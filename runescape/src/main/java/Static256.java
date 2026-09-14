import com.jagex.EntityMoveFlag;
import com.jagex.game.MoveSpeed;
import com.jagex.game.camera.CameraMode;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.seqtype.SeqType;
import com.jagex.game.runetek6.config.seqtype.SeqTypeList;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationType;
import com.jagex.game.runetek6.config.spotanimationtype.SpotAnimationTypeList;
import com.jagex.graphics.ClippingMask;
import com.jagex.graphics.Font;
import com.jagex.graphics.FontMetrics;
import com.jagex.math.Trig1;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static256 {

    @OriginalMember(owner = "client!hu", name = "a", descriptor = "(ZLclient!cg;Z)V")
    public static void movementTick(@OriginalArg(1) PathingEntity entity, @OriginalArg(2) boolean cutscene) {
        @Pc(9) BASType basType = entity.getBASType();
        if (entity.pathPointer == 0) {
            entity.delayedWalkingTicks = 0;
            Static524.entityMoveFlags = 0;
            Static521.entityMoveSpeed = MoveSpeed.STATIONARY;
            return;
        }

        if (entity.actionAnimator.isAnimating() && !entity.actionAnimator.isDelayed()) {
            @Pc(41) SeqType seq = entity.actionAnimator.getAnimation();
            if (entity.animationPathPointer > 0 && seq.animatingPrecedence == 0) {
                Static524.entityMoveFlags = 0;
                Static521.entityMoveSpeed = MoveSpeed.STATIONARY;
                entity.delayedWalkingTicks++;
                return;
            }

            if (entity.animationPathPointer <= 0 && seq.walkingPrecedence == 0) {
                Static521.entityMoveSpeed = MoveSpeed.STATIONARY;
                entity.delayedWalkingTicks++;
                Static524.entityMoveFlags = 0;
                return;
            }
        }

        for (@Pc(86) int i = 0; i < entity.spotAnims.length; i++) {
            if (entity.spotAnims[i].id == -1 || !entity.spotAnims[i].animator.isDelayed()) {
                continue;
            }

            @Pc(117) SpotAnimationType type = SpotAnimationTypeList.instance.list(entity.spotAnims[i].id);

            if (type.loopSeq && type.seq != -1) {
                @Pc(133) SeqType spotSeq = SeqTypeList.instance.list(type.seq);
                if (entity.animationPathPointer > 0 && spotSeq.animatingPrecedence == 0) {
                    Static521.entityMoveSpeed = MoveSpeed.STATIONARY;
                    entity.delayedWalkingTicks++;
                    Static524.entityMoveFlags = 0;
                    return;
                }

                if (entity.animationPathPointer <= 0 && spotSeq.walkingPrecedence == 0) {
                    Static521.entityMoveSpeed = MoveSpeed.STATIONARY;
                    entity.delayedWalkingTicks++;
                    Static524.entityMoveFlags = 0;
                    return;
                }
            }
        }

        @Pc(186) int x = entity.x;
        @Pc(189) int z = entity.z;
        @Pc(206) int pathX = (entity.pathX[entity.pathPointer - 1] * 512) + (entity.getSize() * 256);
        @Pc(222) int pathZ = (entity.pathZ[entity.pathPointer - 1] * 512) + (entity.getSize() * 256);

        if (x < pathX) {
            if (z < pathZ) {
                entity.turn(10240);
            } else if (pathZ < z) {
                entity.turn(14336);
            } else {
                entity.turn(12288);
            }
        } else if (pathX >= x) {
            if (z < pathZ) {
                entity.turn(8192);
            } else if (pathZ < z) {
                entity.turn(0);
            }
        } else if (pathZ > z) {
            entity.turn(6144);
        } else if (pathZ < z) {
            entity.turn(2048);
        } else {
            entity.turn(4096);
        }

        @Pc(348) byte moveSpeed = entity.pathSpeed[entity.pathPointer - 1];
        if (!cutscene && (pathX - x > 1024 || pathX - x < -1024 || pathZ - z > 1024 || pathZ - z < -1024)) {
            entity.z = pathZ;
            entity.x = pathX;
            entity.turn(entity.yawTarget, false);

            Static524.entityMoveFlags = 0;
            if (entity.animationPathPointer > 0) {
                entity.animationPathPointer--;
            }

            Static521.entityMoveSpeed = MoveSpeed.STATIONARY;
            entity.pathPointer--;
            return;
        }

        @Pc(422) int acceleration = 16;
        @Pc(424) boolean crawl = true;
        if (entity instanceof NPCEntity) {
            crawl = ((NPCEntity) entity).type.crawl;
        }

        if (crawl) {
            @Pc(468) int yawDelta = entity.yawTarget - entity.yaw.value;
            if (yawDelta != 0 && entity.target == -1 && entity.yawSpeed != 0) {
                acceleration = 8;
            }
            if (!cutscene && entity.pathPointer > 2) {
                acceleration = 24;
            }
            if (!cutscene && entity.pathPointer > 3) {
                acceleration = 32;
            }
        } else {
            if (!cutscene && entity.pathPointer > 1) {
                acceleration = 24;
            }
            if (!cutscene && entity.pathPointer > 2) {
                acceleration = 32;
            }
        }

        if (entity.delayedWalkingTicks > 0 && entity.pathPointer > 1) {
            entity.delayedWalkingTicks--;
            acceleration = 32;
        }

        if (moveSpeed == MoveSpeed.RUN) {
            acceleration <<= 0x1;
        } else if (moveSpeed == MoveSpeed.CRAWL) {
            acceleration >>= 0x1;
        }

        if (basType.movementAcceleration != -1) {
            acceleration <<= 0x9;

            if (entity.pathPointer == 1) {
                @Pc(468) int accelerationSquared = entity.movementAcceleration * entity.movementAcceleration;
                @Pc(642) int distanceX = (pathX >= entity.x ? pathX - entity.x : entity.x - pathX) << 9;
                @Pc(661) int distanceZ = (pathZ >= entity.z ? pathZ - entity.z : entity.z - pathZ) << 9;
                @Pc(673) int distance = distanceX > distanceZ ? distanceX : distanceZ;
                @Pc(680) int maxAccelerationSquared = distance * basType.movementAcceleration * 2;

                if (accelerationSquared > maxAccelerationSquared) {
                    entity.movementAcceleration /= 2;
                } else if ((accelerationSquared / 2) > distance) {
                    entity.movementAcceleration -= basType.movementAcceleration;

                    if (entity.movementAcceleration < 0) {
                        entity.movementAcceleration = 0;
                    }
                } else if (acceleration > entity.movementAcceleration) {
                    entity.movementAcceleration += basType.movementAcceleration;

                    if (entity.movementAcceleration > acceleration) {
                        entity.movementAcceleration = acceleration;
                    }
                }
            } else if (acceleration > entity.movementAcceleration) {
                entity.movementAcceleration += basType.movementAcceleration;

                if (acceleration < entity.movementAcceleration) {
                    entity.movementAcceleration = acceleration;
                }
            } else if (entity.movementAcceleration > 0) {
                entity.movementAcceleration -= basType.movementAcceleration;

                if (entity.movementAcceleration < 0) {
                    entity.movementAcceleration = 0;
                }
            }

            acceleration = entity.movementAcceleration >> 9;

            if (acceleration < 1) {
                acceleration = 1;
            }
        }

        Static524.entityMoveFlags = 0;

        if (x == pathX && z == pathZ) {
            Static521.entityMoveSpeed = MoveSpeed.STATIONARY;
        } else {
            if (x < pathX) {
                entity.x += acceleration;
                Static524.entityMoveFlags |= EntityMoveFlag.EAST;

                if (entity.x > pathX) {
                    entity.x = pathX;
                }
            } else if (x > pathX) {
                entity.x -= acceleration;
                Static524.entityMoveFlags |= EntityMoveFlag.WEST;

                if (pathX > entity.x) {
                    entity.x = pathX;
                }
            }

            if (acceleration >= 32) {
                Static521.entityMoveSpeed = MoveSpeed.RUN;
            } else {
                Static521.entityMoveSpeed = moveSpeed;
            }

            if (z < pathZ) {
                Static524.entityMoveFlags |= EntityMoveFlag.NORTH;
                entity.z += acceleration;

                if (pathZ < entity.z) {
                    entity.z = pathZ;
                }
            } else if (z > pathZ) {
                entity.z -= acceleration;
                Static524.entityMoveFlags |= EntityMoveFlag.SOUTH;

                if (pathZ > entity.z) {
                    entity.z = pathZ;
                }
            }
        }

        if (entity.x != pathX || pathZ != entity.z) {
            return;
        }

        entity.pathPointer--;

        if (entity.animationPathPointer > 0) {
            entity.animationPathPointer--;
        }
    }

    @OriginalMember(owner = "client!hu", name = "a", descriptor = "(Lclient!da;Ljava/lang/String;Lclient!ve;IIIZLclient!hda;Lclient!aa;III)V")
    public static void drawMapElementText(@OriginalArg(0) Font font, @OriginalArg(1) String text, @OriginalArg(2) FontMetrics metrics, @OriginalArg(3) int offsetX, @OriginalArg(4) int colour, @OriginalArg(5) int spriteHeight, @OriginalArg(7) Component component, @OriginalArg(8) ClippingMask mask, @OriginalArg(9) int drawY, @OriginalArg(10) int offsetY, @OriginalArg(11) int drawX) {
        @Pc(11) int yaw;
        if (Camera.mode == CameraMode.MODE_FOLLOWCOORD) {
            yaw = (int) Camera.playerCameraYaw & 0x3FFF;
        } else {
            yaw = (int) Camera.playerCameraYaw + Camera.yawOffset & 0x3FFF;
        }

        @Pc(33) int radius = Math.max(component.width / 2, component.height / 2) + 10;
        @Pc(59) int distanceSquared = (drawY * drawY) + (drawX * drawX);
        if ((radius * radius) < distanceSquared) {
            return;
        }

        @Pc(74) int sin = Trig1.SIN[yaw];
        @Pc(78) int cos = Trig1.COS[yaw];
        if (Camera.mode != CameraMode.MODE_FOLLOWCOORD) {
            sin = (sin * 256) / (Camera.scaleOffset + 256);
            cos = (cos * 256) / (Camera.scaleOffset + 256);
        }

        @Pc(107) int rotatedX = ((cos * drawX) + (drawY * sin)) >> 14;
        @Pc(118) int rotatedZ = ((cos * drawY) - (drawX * sin)) >> 14;
        @Pc(125) int paraWidth = metrics.paraWidth(null, text, 100);
        @Pc(131) int textX = rotatedX - paraWidth / 2;
        @Pc(139) int textHeight = metrics.stringHeight(100, 0, text, null);
        if (textX >= -component.width && component.width >= textX && rotatedZ >= -component.height && component.height >= rotatedZ) {
            font.renderLines(text, (component.width / 2) + textX + offsetX, ((component.height / 2) + offsetY) - rotatedZ - spriteHeight - textHeight, offsetX, offsetY, paraWidth, 50, 1, 0, 0, colour, 0, mask, null, null);
        }
    }
}
