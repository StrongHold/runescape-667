import com.jagex.Client;
import com.jagex.IndexedImage;
import com.jagex.core.constants.HintArrowType;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.defaults.GraphicsDefaults;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkType;
import com.jagex.game.runetek6.config.hitmarktype.HitmarkTypeList;
import com.jagex.game.runetek6.config.npctype.NPCType;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.graphics.Font;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.Fonts;
import com.jagex.graphics.Sprite;
import com.jagex.graphics.Toolkit;
import com.jagex.js5.js5;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class OverlayManager {

    @OriginalMember(owner = "client!lc", name = "B", descriptor = "I")
    public static final int MAX_CHAT_LINES = 50;

    @OriginalMember(owner = "client!lc", name = "w", descriptor = "[I")
    public static final int[] chatlineWidths = new int[MAX_CHAT_LINES];

    @OriginalMember(owner = "client!lc", name = "v", descriptor = "[I")
    public static final int[] chatlineX = new int[MAX_CHAT_LINES];

    @OriginalMember(owner = "client!lc", name = "z", descriptor = "[I")
    public static final int[] chatlineY = new int[MAX_CHAT_LINES];

    @OriginalMember(owner = "client!lc", name = "E", descriptor = "[Lclient!dj;")
    public static final EntityChatLine[] chatLines = new EntityChatLine[MAX_CHAT_LINES];

    @OriginalMember(owner = "client!gm", name = "x", descriptor = "[I")
    public static final int[] hitmarkpos = new int[3];

    @OriginalMember(owner = "client!pd", name = "r", descriptor = "I")
    public static int chatLineCount = 0;

    @OriginalMember(owner = "client!td", name = "a", descriptor = "(IIIIIIZ)V")
    public static void render(@OriginalArg(0) int screenY, @OriginalArg(1) int screenWidth, @OriginalArg(4) int screenX, @OriginalArg(5) int screenHeight) {
        @Pc(7) int playerCount = PlayerList.highResolutionCount;
        chatLineCount = 0;
        @Pc(11) int[] playerIndices = PlayerList.highResolutionSlots;

        @Pc(20) int count;
        if (CutsceneManager.state == 3) {
            count = CutsceneManager.actors.length;
        } else {
            count = NPCList.size + playerCount;
        }

        for (@Pc(27) int i = 0; i < count; i++) {
            @Pc(31) NPCType npcType = null;
            @Pc(51) PathingEntity entity;

            if (CutsceneManager.state == 3) {
                @Pc(111) Actor actor = CutsceneManager.actors[i];
                if (!actor.initialised) {
                    continue;
                }

                entity = actor.entity();

                if (Static198.anInt3276 != entity.anInt10704) {
                    continue;
                }

                if (actor.npcId >= 0) {
                    npcType = ((NPCEntity) entity).type;

                    if (npcType.multinpcs != null) {
                        npcType = npcType.getMultiNPC(TimedVarDomain.instance);

                        if (npcType == null) {
                            continue;
                        }
                    }
                }
            } else {
                if (i < playerCount) {
                    entity = PlayerList.highResolutionPlayers[playerIndices[i]];
                } else {
                    entity = ((NPCEntityNode) NPCList.local.get(NPCList.slots[i - playerCount])).npc;
                    npcType = ((NPCEntity) entity).type;

                    if (npcType.multinpcs != null) {
                        npcType = npcType.getMultiNPC(TimedVarDomain.instance);

                        if (npcType == null) {
                            continue;
                        }
                    }
                }

                if (entity.drawPriority < 0 || entity.anInt10704 != Static198.anInt3276 && PlayerEntity.self.level != entity.level) {
                    continue;
                }
            }

            Static716.method9350(entity, screenHeight >> 1, entity.getY(), screenWidth >> 1);

            if (hitmarkpos[0] >= 0) {
                if (entity.enableMessages()) {
                    @Pc(182) EntityChatLine chatLine = entity.getChatLine(-3109);

                    if (chatLine != null && chatLineCount < MAX_CHAT_LINES) {
                        chatlineWidths[chatLineCount] = Fonts.b12Metrics.stringWidth(chatLine.getText()) / 2;
                        chatlineX[chatLineCount] = hitmarkpos[0];
                        chatlineY[chatLineCount] = hitmarkpos[1];
                        chatLines[chatLineCount] = chatLine;
                        chatLineCount++;
                    }
                }

                @Pc(233) int y = screenY + hitmarkpos[1];

                if (entity.visible || TimeUtils.clock >= entity.healthClock) {
                    y -= Math.max(Fonts.b12Metrics.paddingTop, Sprites.hitbarDefault[0].getHeight());
                } else {
                    @Pc(262) byte icon = 1;
                    @Pc(267) int hitbarSprite;

                    if (npcType == null) {
                        @Pc(283) PlayerEntity local283 = PlayerList.highResolutionPlayers[playerIndices[i]];
                        hitbarSprite = entity.getBASType().hitbarSprite;

                        if (local283.showPIcon) {
                            icon = 2;
                        }
                    } else {
                        hitbarSprite = npcType.healthBarSprite;

                        if (hitbarSprite == -1) {
                            hitbarSprite = entity.getBASType().hitbarSprite;
                        }
                    }

                    @Pc(295) Sprite[] hitbarDefault = Sprites.hitbarDefault;
                    if (hitbarSprite != -1) {
                        @Pc(306) Sprite[] sprites = (Sprite[]) Sprites.hitbarCache.get(hitbarSprite);

                        if (sprites == null) {
                            @Pc(313) IndexedImage[] images = IndexedImage.load(js5.SPRITES, hitbarSprite, 0);

                            if (images != null) {
                                sprites = new Sprite[images.length];

                                for (@Pc(321) int j = 0; j < images.length; j++) {
                                    sprites[j] = Toolkit.active.createSprite(images[j], true);
                                }

                                Sprites.hitbarCache.put(sprites, hitbarSprite);
                            }
                        }

                        if (sprites != null && sprites.length >= 2) {
                            hitbarDefault = sprites;
                        }
                    }

                    if (icon >= hitbarDefault.length) {
                        icon = 1;
                    }

                    @Pc(377) Sprite hitbarFirst = hitbarDefault[0];
                    @Pc(381) Sprite hitbarIcon = hitbarDefault[icon];

                    y -= Math.max(Fonts.b12Metrics.paddingTop, hitbarFirst.getHeight());
                    @Pc(321) int x = hitmarkpos[0] + screenX - (hitbarFirst.getWidth() >> 1);

                    @Pc(409) int width = hitbarFirst.getWidth() * entity.healthPercentage / 255;
                    @Pc(412) int height = hitbarFirst.getHeight();

                    if (entity.healthPercentage > 0 && width < 2) {
                        width = 2;
                    }

                    hitbarFirst.render(x, y);
                    Toolkit.active.T(x, y, width + x, y - -height);

                    hitbarIcon.render(x, y);
                    Toolkit.active.KA(screenX, screenY, screenWidth + screenX, screenY + screenHeight);

                    OrthoMode.method8927(x, hitbarFirst.scaleWidth() + x, y, height + y);
                }

                y -= 2;

                if (!entity.visible) {
                    if (entity.timerbarEnd > TimeUtils.clock) {
                        @Pc(486) Sprite bottom = Sprites.timerbarDefault[entity.timerbarSprite ? 2 : 0];
                        @Pc(496) Sprite top = Sprites.timerbarDefault[entity.timerbarSprite ? 3 : 1];

                        @Pc(504) int timerbarSprite;
                        if (entity instanceof NPCEntity) {
                            timerbarSprite = npcType.timerbarSprite;

                            if (timerbarSprite == -1) {
                                timerbarSprite = entity.getBASType().timerbarSprite;
                            }
                        } else {
                            timerbarSprite = entity.getBASType().timerbarSprite;
                        }

                        if (timerbarSprite != -1) {
                            @Pc(306) Sprite[] sprites = (Sprite[]) Sprites.timerbarCache.get(timerbarSprite);

                            if (sprites == null) {
                                @Pc(313) IndexedImage[] images = IndexedImage.load(js5.SPRITES, timerbarSprite, 0);

                                if (images != null) {
                                    sprites = new Sprite[images.length];

                                    for (@Pc(321) int j = 0; j < images.length; j++) {
                                        sprites[j] = Toolkit.active.createSprite(images[j], true);
                                    }

                                    Sprites.timerbarCache.put(sprites, timerbarSprite);
                                }
                            }

                            if (sprites != null && sprites.length == 4) {
                                bottom = sprites[entity.timerbarSprite ? 2 : 0];
                                top = sprites[entity.timerbarSprite ? 3 : 1];
                            }
                        }

                        @Pc(612) int remaining = entity.timerbarEnd - TimeUtils.clock;
                        @Pc(651) int spriteWidth;
                        if (entity.timerbarStart < remaining) {
                            remaining -= entity.timerbarStart;
                            @Pc(321) int granularity = entity.timerbarGranularity == 0 ? 0 : (((entity.timerbarDuration - remaining) / entity.timerbarGranularity) * entity.timerbarGranularity);
                            spriteWidth = bottom.getWidth() * granularity / entity.timerbarDuration;
                        } else {
                            spriteWidth = bottom.getWidth();
                        }

                        @Pc(321) int spriteHeight = bottom.getHeight();
                        y -= spriteHeight;

                        @Pc(409) int x = hitmarkpos[0] + screenX - (bottom.getWidth() >> 1);
                        bottom.render(x, y);

                        Toolkit.active.T(x, y, spriteWidth + x, y + spriteHeight);
                        top.render(x, y);

                        Toolkit.active.KA(screenX, screenY, screenX + screenWidth, screenY + screenHeight);
                        OrthoMode.method8927(x, bottom.scaleWidth() + x, y, spriteHeight + y);

                        y -= 2;
                    }

                    if (npcType != null) {
                        if (npcType.headIcon >= 0 && Sprites.headiconsPrayer.length > npcType.headIcon) {
                            @Pc(486) Sprite headicon = Sprites.headiconsPrayer[npcType.headIcon];
                            y -= headicon.getHeight();

                            headicon.render(screenX + hitmarkpos[0] - (headicon.getWidth() >> 1), y);
                            OrthoMode.method8927(screenX + hitmarkpos[0] - (headicon.getWidth() >> 1), hitmarkpos[0] + screenX - (headicon.getWidth() >> 1) + headicon.scaleWidth(), y, headicon.scaleHeight() + y);

                            y -= 2;
                        }
                    } else {
                        @Pc(720) PlayerEntity player = (PlayerEntity) entity;

                        if (player.pkIcon != -1) {
                            @Pc(496) Sprite headicon = Sprites.headiconsPk[player.pkIcon];
                            y -= headicon.getHeight();

                            headicon.render(hitmarkpos[0] + screenX - 12, y);
                            OrthoMode.method8927(hitmarkpos[0] + screenX + -12, hitmarkpos[0] + screenX + headicon.scaleWidth() - 12, y, headicon.scaleHeight() + y);

                            y -= 2;
                        }

                        if (player.prayerIcon != -1) {
                            @Pc(496) Sprite headicon = Sprites.headiconsPrayer[player.prayerIcon];
                            y -= headicon.getHeight();

                            headicon.render(screenX + hitmarkpos[0] - 12, y);
                            OrthoMode.method8927(hitmarkpos[0] + screenX + -12, hitmarkpos[0] + screenX + headicon.scaleWidth() - 12, y, y + headicon.scaleHeight());

                            y -= 2;
                        }
                    }
                }

                if (entity instanceof PlayerEntity) {
                    if (i >= 0) {
                        @Pc(267) int maxHeight = 0;
                        @Pc(905) HintArrow[] hintArrows = Static527.hintArrows;

                        for (@Pc(504) int j = 0; j < hintArrows.length; j++) {
                            @Pc(913) HintArrow hintArrow = hintArrows[j];

                            if (hintArrow != null && hintArrow.type == HintArrowType.PLAYER && hintArrow.entity == playerIndices[i]) {
                                @Pc(381) Sprite headicon = Sprites.hintHeadicons[hintArrow.sprite];
                                if (headicon.getHeight() > maxHeight) {
                                    maxHeight = headicon.getHeight();
                                }

                                headicon.render(hitmarkpos[0] + screenX - 12, y - headicon.getHeight());
                                OrthoMode.method8927(screenX + hitmarkpos[0] - 12, hitmarkpos[0] + (screenX - 12) - -headicon.scaleWidth(), y - headicon.getHeight(), y - headicon.getHeight() - -headicon.scaleHeight());
                            }
                        }

                        if (maxHeight > 0) {
                            y -= maxHeight + 2;
                        }
                    }
                } else {
                    @Pc(267) int maxHeight = 0;
                    @Pc(905) HintArrow[] hintArrows = Static527.hintArrows;

                    for (@Pc(504) int j = 0; j < hintArrows.length; j++) {
                        @Pc(913) HintArrow hintArrow = hintArrows[j];

                        if (hintArrow != null && hintArrow.type == HintArrowType.NPC && NPCList.slots[i - playerCount] == hintArrow.entity) {
                            @Pc(381) Sprite headicon = Sprites.hintHeadicons[hintArrow.sprite];
                            if (headicon.getHeight() > maxHeight) {
                                maxHeight = headicon.getHeight();
                            }

                            @Pc(1071) boolean draw;
                            if (hintArrow.flashRate == 0) {
                                draw = true;
                            } else {
                                @Pc(409) int time = (GameShell.speed() * 1000) / hintArrow.flashRate / 2;
                                draw = time > TimeUtils.clock % (time * 2);
                            }

                            if (draw) {
                                headicon.render(hitmarkpos[0] + screenX - 12, -headicon.getHeight() + y);
                                OrthoMode.method8927(screenX + hitmarkpos[0] - 12, screenX + hitmarkpos[0] - (12 - headicon.scaleWidth()), y - headicon.getHeight(), -headicon.getHeight() + (y - -headicon.scaleHeight()));
                            }
                        }
                    }

                    if (maxHeight > 0) {
                        y -= maxHeight + 2;
                    }
                }

                for (@Pc(267) int j = 0; j < GraphicsDefaults.instance.maxhitmarks; j++) {
                    @Pc(1179) int endTime = entity.hitmarkEndTimes[j];
                    @Pc(504) int damageTypeId = entity.damageHitmarkTypes[j];

                    @Pc(1186) HitmarkType damageType = null;
                    @Pc(651) int duration = 0;
                    if (damageTypeId >= 0) {
                        if (TimeUtils.clock >= endTime) {
                            continue;
                        }

                        damageType = HitmarkTypeList.instance.list(entity.damageHitmarkTypes[j]);
                        duration = damageType.duration;
                    } else if (endTime < 0) {
                        continue;
                    }

                    @Pc(321) int soakTypeId = entity.soakHitmarkTypes[j];
                    @Pc(1218) HitmarkType soakType = null;
                    if (soakTypeId >= 0) {
                        soakType = HitmarkTypeList.instance.list(soakTypeId);
                    }

                    if (endTime - duration <= TimeUtils.clock) {
                        @Pc(412) int healthPercentage = entity.healthPercentages[j];
                        if (healthPercentage >= 0) {
                            entity.healthClock = TimeUtils.clock + 300;
                            entity.healthPercentage = healthPercentage;
                            entity.healthPercentages[j] = -1;
                        }

                        if (damageType == null) {
                            entity.hitmarkEndTimes[j] = -1;
                        } else {
                            @Pc(1274) int midY = entity.getY() / 2;
                            Static716.method9350(entity, screenHeight >> 1, midY, screenWidth >> 1);

                            if (hitmarkpos[0] > -1) {
                                hitmarkpos[0] += GraphicsDefaults.instance.hitmarkpos_x[j];
                                hitmarkpos[1] += GraphicsDefaults.instance.hitmarkpos_y[j];
                                @Pc(1321) int damageIconWidth = 0;
                                @Pc(1323) int damageInnerWidth = 0;
                                @Pc(1325) int damageLeftWidth = 0;
                                @Pc(1327) int damageRightWidth = 0;
                                @Pc(1329) int damageIconOffsetX = 0;
                                @Pc(1331) int damageInnerOffsetX = 0;
                                @Pc(1333) int damageLeftOffsetX = 0;
                                @Pc(1335) int damageRightOffsetX = 0;
                                @Pc(1337) Sprite soakIcon = null;
                                @Pc(1339) Sprite soakInner = null;
                                @Pc(1341) Sprite soakLeft = null;
                                @Pc(1343) Sprite soakRight = null;
                                @Pc(1345) int soakIconWidth = 0;
                                @Pc(1347) int soakInnerWidth = 0;
                                @Pc(1349) int soakLeftWidth = 0;
                                @Pc(1351) int soakRightWidth = 0;
                                @Pc(1353) int soakIconOffsetX = 0;
                                @Pc(1355) int soakInnerOffsetX = 0;
                                @Pc(1357) int soakLeftOffsetX = 0;
                                @Pc(1359) int soakRightOffsetX = 0;
                                @Pc(1364) Sprite damageIcon = damageType.getIcon(Toolkit.active);
                                @Pc(1366) int maxHeight = 0;

                                if (damageIcon != null) {
                                    damageIconWidth = damageIcon.getWidth();
                                    @Pc(1374) int iconHeight = damageIcon.getHeight();
                                    damageIcon.projectOffsets(Static167.hitmarkOffsets);
                                    if (iconHeight > 0) {
                                        maxHeight = iconHeight;
                                    }
                                    damageIconOffsetX = Static167.hitmarkOffsets[0];
                                }

                                @Pc(1391) Sprite damageInner = damageType.getInner(Toolkit.active);
                                if (damageInner != null) {
                                    damageInnerWidth = damageInner.getWidth();
                                    @Pc(1374) int iconHeight = damageInner.getHeight();
                                    if (iconHeight > maxHeight) {
                                        maxHeight = iconHeight;
                                    }
                                    damageInner.projectOffsets(Static167.hitmarkOffsets);
                                    damageInnerOffsetX = Static167.hitmarkOffsets[0];
                                }

                                @Pc(1420) Sprite damageLeft = damageType.getLeft(Toolkit.active);
                                if (damageLeft != null) {
                                    damageLeftWidth = damageLeft.getWidth();
                                    @Pc(1374) int iconHeight = damageLeft.getHeight();
                                    if (iconHeight > maxHeight) {
                                        maxHeight = iconHeight;
                                    }
                                    damageLeft.projectOffsets(Static167.hitmarkOffsets);
                                    damageLeftOffsetX = Static167.hitmarkOffsets[0];
                                }

                                @Pc(1449) Sprite damageRight = damageType.getRight(Toolkit.active);
                                if (damageRight != null) {
                                    damageRightWidth = damageRight.getWidth();
                                    @Pc(1374) int iconHeight = damageRight.getHeight();
                                    damageRight.projectOffsets(Static167.hitmarkOffsets);
                                    if (iconHeight > maxHeight) {
                                        maxHeight = iconHeight;
                                    }
                                    damageRightOffsetX = Static167.hitmarkOffsets[0];
                                }

                                if (soakType != null) {
                                    soakIcon = soakType.getIcon(Toolkit.active);

                                    if (soakIcon != null) {
                                        soakIconWidth = soakIcon.getWidth();
                                        @Pc(1374) int iconHeight = soakIcon.getHeight();
                                        if (maxHeight < iconHeight) {
                                            maxHeight = iconHeight;
                                        }
                                        soakIcon.projectOffsets(Static167.hitmarkOffsets);
                                        soakIconOffsetX = Static167.hitmarkOffsets[0];
                                    }

                                    soakInner = soakType.getInner(Toolkit.active);
                                    if (soakInner != null) {
                                        soakInnerWidth = soakInner.getWidth();
                                        @Pc(1374) int iconHeight = soakInner.getHeight();
                                        if (iconHeight > maxHeight) {
                                            maxHeight = iconHeight;
                                        }
                                        soakInner.projectOffsets(Static167.hitmarkOffsets);
                                        soakInnerOffsetX = Static167.hitmarkOffsets[0];
                                    }

                                    soakLeft = soakType.getLeft(Toolkit.active);
                                    if (soakLeft != null) {
                                        soakLeftWidth = soakLeft.getWidth();
                                        @Pc(1374) int iconHeight = soakLeft.getHeight();
                                        soakLeft.projectOffsets(Static167.hitmarkOffsets);
                                        if (maxHeight < iconHeight) {
                                            maxHeight = iconHeight;
                                        }
                                        soakLeftOffsetX = Static167.hitmarkOffsets[0];
                                    }

                                    soakRight = soakType.getRight(Toolkit.active);
                                    if (soakRight != null) {
                                        soakRightWidth = soakRight.getWidth();
                                        @Pc(1374) int iconHeight = soakRight.getHeight();
                                        soakRight.projectOffsets(Static167.hitmarkOffsets);
                                        if (maxHeight < iconHeight) {
                                            maxHeight = iconHeight;
                                        }
                                        soakRightOffsetX = Static167.hitmarkOffsets[0];
                                    }
                                }

                                @Pc(1589) Font hitFont = Fonts.p11;
                                @Pc(1591) Font soakFont = Fonts.p11;

                                @Pc(1593) FontMetrics hitFontMetrics = Fonts.p11Metrics;
                                @Pc(1595) FontMetrics soakFontMetrics = Fonts.p11Metrics;

                                @Pc(1374) int damageFont = damageType.font;

                                if (damageFont >= 0) {
                                    @Pc(1607) Font font = Fonts.font(true, true, damageFont, Toolkit.active);
                                    @Pc(1612) FontMetrics metrics = Fonts.metrics(damageFont, Toolkit.active);

                                    if (font != null && metrics != null) {
                                        hitFontMetrics = metrics;
                                        hitFont = font;
                                    }
                                }

                                if (soakType != null) {
                                    damageFont = soakType.font;

                                    if (damageFont >= 0) {
                                        @Pc(1607) Font font = Fonts.font(true, true, damageFont, Toolkit.active);
                                        @Pc(1612) FontMetrics metrics = Fonts.metrics(damageFont, Toolkit.active);

                                        if (font != null && metrics != null) {
                                            soakFontMetrics = metrics;
                                            soakFont = font;
                                        }
                                    }
                                }

                                @Pc(1658) String soakAmountText = null;
                                @Pc(1662) int soakAmountWidth = 0;

                                @Pc(1670) String damageTypeText = damageType.method6457(entity.hitAmounts[j]);
                                @Pc(1675) int damageAmountWidth = hitFontMetrics.stringWidth(damageTypeText);

                                if (soakType != null) {
                                    soakAmountText = soakType.method6457(entity.soakAmounts[j]);
                                    soakAmountWidth = soakFontMetrics.stringWidth(soakAmountText);
                                }

                                @Pc(1692) int damageTiles = 0;
                                if (damageInnerWidth > 0) {
                                    damageTiles = damageAmountWidth / damageInnerWidth + 1;
                                }

                                @Pc(1702) int soakTiles = 0;
                                if (soakType != null && soakInnerWidth > 0) {
                                    soakTiles = soakAmountWidth / soakInnerWidth + 1;
                                }

                                @Pc(1719) int local1719 = 0;
                                if (damageIconWidth > 0) {
                                    local1719 = damageIconWidth;
                                }

                                local1719 += 2;

                                @Pc(1730) int local1730 = local1719;
                                if (damageLeftWidth > 0) {
                                    local1719 += damageLeftWidth;
                                }

                                @Pc(1741) int local1741 = local1719;
                                @Pc(1743) int local1743 = local1719;
                                @Pc(1752) int local1752;
                                if (damageInnerWidth > 0) {
                                    local1752 = damageTiles * damageInnerWidth;
                                    local1719 += local1752;
                                    local1743 += (local1752 - damageAmountWidth) / 2;
                                } else {
                                    local1719 += damageAmountWidth;
                                }

                                local1752 = local1719;

                                if (damageRightWidth > 0) {
                                    local1719 += damageRightWidth;
                                }

                                @Pc(1784) int local1784 = 0;
                                @Pc(1786) int local1786 = 0;
                                @Pc(1788) int local1788 = 0;
                                @Pc(1790) int local1790 = 0;
                                @Pc(1792) int local1792 = 0;
                                if (soakType != null) {
                                    local1719 += 2;
                                    local1784 = local1719;
                                    if (soakIconWidth > 0) {
                                        local1719 += soakIconWidth;
                                    }
                                    local1719 += 2;
                                    local1786 = local1719;
                                    if (soakLeftWidth > 0) {
                                        local1719 += soakLeftWidth;
                                    }
                                    local1792 = local1719;
                                    local1788 = local1719;
                                    if (soakInnerWidth <= 0) {
                                        local1719 += soakAmountWidth;
                                    } else {
                                        @Pc(1831) int local1831 = soakTiles * soakInnerWidth;
                                        local1792 = local1719 + (local1831 - soakAmountWidth) / 2;
                                        local1719 += local1831;
                                    }
                                    local1790 = local1719;
                                    if (soakRightWidth > 0) {
                                        local1719 += soakRightWidth;
                                    }
                                }

                                @Pc(1831) int timeRemaining = entity.hitmarkEndTimes[j] - TimeUtils.clock;
                                @Pc(1871) int local1871 = damageType.offsetX - damageType.offsetX * timeRemaining / damageType.duration;
                                @Pc(1884) int local1884 = timeRemaining * damageType.offsetY / damageType.duration - damageType.offsetY;
                                @Pc(1898) int local1898 = screenX + hitmarkpos[0] + local1871 - (local1719 >> 1);
                                @Pc(1908) int local1908 = hitmarkpos[1] + screenY + local1884 - 12;
                                @Pc(1910) int local1910 = local1908;
                                @Pc(1915) int local1915 = local1908 + maxHeight;
                                @Pc(1922) int local1922 = damageType.textOffsetY + local1908 + 15;

                                @Pc(1928) int local1928 = local1922 - hitFontMetrics.paddingTop;
                                if (local1928 < local1908) {
                                    local1910 = local1928;
                                }

                                @Pc(1942) int local1942 = hitFontMetrics.paddingBottom + local1922;
                                if (local1915 < local1942) {
                                    local1915 = local1942;
                                }

                                @Pc(1949) int local1949 = 0;
                                @Pc(1963) int local1963;
                                @Pc(1969) int local1969;
                                if (soakType != null) {
                                    local1949 = soakType.textOffsetY + local1908 + 15;
                                    local1963 = local1949 - soakFontMetrics.paddingTop;
                                    local1969 = local1949 + soakFontMetrics.paddingBottom;
                                    if (local1910 > local1963) {
                                        local1910 = local1963;
                                    }
                                    if (local1915 < local1969) {
                                        local1915 = local1969;
                                    }
                                }

                                local1963 = 255;

                                if (damageType.fadeTime >= 0) {
                                    local1963 = (timeRemaining << 8) / (damageType.duration - damageType.fadeTime);
                                }

                                if (local1963 >= 0 && local1963 < 255) {
                                    local1969 = local1963 << 24;

                                    @Pc(2017) int local2017 = local1969 | 0xFFFFFF;
                                    if (damageIcon != null) {
                                        damageIcon.render(local1898 - damageIconOffsetX, local1908, 0, local2017, 1);
                                    }
                                    if (damageLeft != null) {
                                        damageLeft.render(local1730 + local1898 - damageLeftOffsetX, local1908, 0, local2017, 1);
                                    }

                                    @Pc(2053) int local2053;
                                    if (damageInner != null) {
                                        for (local2053 = 0; local2053 < damageTiles; local2053++) {
                                            damageInner.render(local1741 + local1898 + damageInnerWidth * local2053 - damageInnerOffsetX, local1908, 0, local2017, 1);
                                        }
                                    }
                                    if (damageRight != null) {
                                        damageRight.render(local1898 + local1752 - damageRightOffsetX, local1908, 0, local2017, 1);
                                    }

                                    hitFont.render(damageTypeText, local1898 + local1743, local1922, 0, local1969 | damageType.textColour);

                                    if (soakType != null) {
                                        if (soakIcon != null) {
                                            soakIcon.render(local1784 + local1898 - soakIconOffsetX, local1908, 0, local2017, 1);
                                        }
                                        if (soakLeft != null) {
                                            soakLeft.render(local1898 + local1786 - soakLeftOffsetX, local1908, 0, local2017, 1);
                                        }
                                        if (soakInner != null) {
                                            for (local2053 = 0; local2053 < soakTiles; local2053++) {
                                                soakInner.render(local1898 + local1788 + soakInnerWidth * local2053 - soakInnerOffsetX, local1908, 0, local2017, 1);
                                            }
                                        }
                                        if (soakRight != null) {
                                            soakRight.render(local1898 + local1790 - soakRightOffsetX, local1908, 0, local2017, 1);
                                        }
                                        soakFont.render(soakAmountText, local1792 + local1898, local1949, 0, soakType.textColour | local1969);
                                    }
                                } else {
                                    if (damageIcon != null) {
                                        damageIcon.render(local1898 - damageIconOffsetX, local1908);
                                    }
                                    if (damageLeft != null) {
                                        damageLeft.render(local1898 + local1730 - damageLeftOffsetX, local1908);
                                    }
                                    if (damageInner != null) {
                                        for (local1969 = 0; local1969 < damageTiles; local1969++) {
                                            damageInner.render(damageInnerWidth * local1969 + local1741 + local1898 - damageInnerOffsetX, local1908);
                                        }
                                    }
                                    if (damageRight != null) {
                                        damageRight.render(local1752 + local1898 - damageRightOffsetX, local1908);
                                    }

                                    hitFont.render(damageTypeText, local1898 + local1743, local1922, 0, damageType.textColour | 0xFF000000);

                                    if (soakType != null) {
                                        if (soakIcon != null) {
                                            soakIcon.render(local1784 + local1898 - soakIconOffsetX, local1908);
                                        }
                                        if (soakLeft != null) {
                                            soakLeft.render(local1898 + local1786 - soakLeftOffsetX, local1908);
                                        }
                                        if (soakInner != null) {
                                            for (local1969 = 0; local1969 < soakTiles; local1969++) {
                                                soakInner.render(local1898 + local1788 + soakInnerWidth * local1969 - soakInnerOffsetX, local1908);
                                            }
                                        }
                                        if (soakRight != null) {
                                            soakRight.render(local1790 + local1898 - soakRightOffsetX, local1908);
                                        }
                                        soakFont.render(soakAmountText, local1792 + local1898, local1949, 0, soakType.textColour | 0xFF000000);
                                    }
                                }

                                OrthoMode.method8927(local1898, local1719 + local1898, local1910, local1915 + 1);
                            }
                        }
                    }
                }
            }
        }

        @Pc(2422) int local2422;
        for (@Pc(2403) int local2403 = 0; local2403 < Static172.anInt2893; local2403++) {
            local2422 = Static324.anIntArray390[local2403];
            @Pc(2429) PathingEntity local2429;
            if (local2422 < 2048) {
                local2429 = PlayerList.highResolutionPlayers[local2422];
            } else {
                local2429 = ((NPCEntityNode) NPCList.local.get(local2422 - 2048)).npc;
            }
            @Pc(267) int local267 = Static212.anIntArray283[local2403];
            @Pc(2452) PathingEntity local2452;
            if (local267 < 2048) {
                local2452 = PlayerList.highResolutionPlayers[local267];
            } else {
                local2452 = ((NPCEntityNode) NPCList.local.get(local267 - 2048)).npc;
            }
            Static397.method5557(screenX, local2429, local2452, screenHeight, screenWidth, screenY, --local2429.anInt10735);
        }

        local2422 = Fonts.b12Metrics.paddingTop + Fonts.b12Metrics.paddingBottom + 2;

        for (@Pc(233) int i = 0; i < chatLineCount; i++) {
            @Pc(267) int lineX = chatlineX[i];
            @Pc(1179) int lineY = chatlineY[i];
            @Pc(504) int lineWidth = chatlineWidths[i];

            @Pc(2517) boolean local2517 = true;
            while (local2517) {
                local2517 = false;

                for (@Pc(651) int j = 0; j < i; j++) {
                    if (lineY + 2 > -local2422 + chatlineY[j] && lineY - local2422 < chatlineY[j] - -2 && chatlineX[j] + chatlineWidths[j] > lineX - lineWidth && lineX + lineWidth > chatlineX[j] + -chatlineWidths[j] && lineY > chatlineY[j] - local2422) {
                        lineY = chatlineY[j] - local2422;
                        local2517 = true;
                    }
                }
            }

            chatlineY[i] = lineY;

            @Pc(2627) String local2627 = chatLines[i].getText();
            @Pc(321) int local321 = Fonts.b12Metrics.stringWidth(local2627);
            @Pc(409) int local409 = lineX + screenX;
            @Pc(412) int local412 = lineY + screenY - Fonts.b12Metrics.paddingTop;
            @Pc(1274) int local1274 = local321 + local409;
            @Pc(2654) int local2654 = Fonts.b12Metrics.paddingBottom + screenY + lineY;

            if (Client.disableChatEffects == 0) {
                @Pc(2663) int rgb = 0xFFFF00;
                @Pc(2669) int colour = chatLines[i].getColour();
                if (colour < 6) {
                    rgb = Static587.anIntArray689[colour];
                }
                if (colour == 6) {
                    rgb = Static198.anInt3276 % 20 < 10 ? 16711680 : 16776960;
                }
                if (colour == 7) {
                    rgb = Static198.anInt3276 % 20 >= 10 ? 65535 : 255;
                }
                if (colour == 8) {
                    rgb = Static198.anInt3276 % 20 >= 10 ? 8454016 : 45056;
                }

                @Pc(2749) int local2749;
                if (colour == 9) {
                    local2749 = 150 - chatLines[i].getRemaining() * 150 / chatLines[i].getDuration();
                    if (local2749 < 50) {
                        rgb = local2749 * 1280 + 16711680;
                    } else if (local2749 < 100) {
                        rgb = 33160960 - local2749 * 327680;
                    } else if (local2749 < 150) {
                        rgb = local2749 * 5 + 65280 - 500;
                    }
                }

                if (colour == 10) {
                    local2749 = 150 - chatLines[i].getRemaining() * 150 / chatLines[i].getDuration();
                    if (local2749 < 50) {
                        rgb = local2749 * 5 + 16711680;
                    } else if (local2749 < 100) {
                        rgb = 16711935 - (local2749 - 50) * 327680;
                    } else if (local2749 < 150) {
                        rgb = local2749 * 327680 + 500 + 255 - local2749 * 5 - 32768000;
                    }
                }

                if (colour == 11) {
                    local2749 = 150 - chatLines[i].getRemaining() * 150 / chatLines[i].getDuration();
                    if (local2749 < 50) {
                        rgb = 16777215 - local2749 * 327685;
                    } else if (local2749 < 100) {
                        rgb = local2749 * 327685 + 65280 - 16384250;
                    } else if (local2749 < 150) {
                        rgb = 16777215 - (local2749 - 100) * 327680;
                    }
                }

                local2749 = rgb | 0xFF000000;

                @Pc(1321) int effect = chatLines[i].getEffect();
                if (effect == 0) {
                    Fonts.b12.renderCentre(local2627, lineX + screenX, lineY + screenY, local2749, -16777216);
                    local1274 -= local321 >> 1;
                    local409 -= local321 >> 1;
                }

                if (effect == 1) {
                    local2654 += 5;
                    Fonts.b12.renderWave(local2749, Static198.anInt3276, local2627, screenX + lineX, lineY + screenY, -16777216);
                    local1274 -= local321 >> 1;
                    local409 -= local321 >> 1;
                    local412 -= 5;
                }

                if (effect == 2) {
                    local412 -= 5;
                    Fonts.b12.renderWave2(-16777216, Static198.anInt3276, local2627, screenX + lineX, local2749, screenY + lineY);
                    local2654 += 5;
                    local409 -= (local321 >> 1) + 5;
                    local1274 -= (local321 >> 1) - 5;
                }

                if (effect == 3) {
                    @Pc(1323) int local1323 = 150 - chatLines[i].getRemaining() * 150 / chatLines[i].getDuration();
                    local409 -= local321 >> 1;
                    local2654 += 7;
                    local412 -= 7;
                    local1274 -= local321 >> 1;
                    Fonts.b12.renderShake(local1323, lineX + screenX, Static198.anInt3276, screenY + lineY, local2749, local2627, -16777216);
                }

                if (effect == 4) {
                    @Pc(1323) int local1323 = 150 - chatLines[i].getRemaining() * 150 / chatLines[i].getDuration();
                    @Pc(1325) int local1325 = (Fonts.b12Metrics.stringWidth(local2627) + 100) * local1323 / 150;
                    Toolkit.active.T(screenX + lineX - 50, screenY, lineX + screenX + 50, screenY - -screenHeight);
                    local409 += 50 - local1325;
                    local1274 += 50 - local1325;
                    Fonts.b12.render(local2627, screenX + lineX + 50 - local1325, lineY + screenY, 0xFF000000, local2749);
                    Toolkit.active.KA(screenX, screenY, screenX + screenWidth, screenHeight + screenY);
                }

                if (effect == 5) {
                    @Pc(1323) int local1323 = 150 - chatLines[i].getRemaining() * 150 / chatLines[i].getDuration();
                    @Pc(1325) int local1325 = 0;
                    if (local1323 < 25) {
                        local1325 = local1323 - 25;
                    } else if (local1323 > 125) {
                        local1325 = local1323 - 125;
                    }

                    @Pc(1327) int local1327 = Fonts.b12Metrics.paddingBottom + Fonts.b12Metrics.paddingTop;
                    Toolkit.active.T(screenX, screenY + lineY - local1327 - 1, screenWidth + screenX, lineY + screenY + 5);
                    local2654 += local1325;
                    local409 -= local321 >> 1;
                    local1274 -= local321 >> 1;
                    local412 += local1325;
                    Fonts.b12.renderCentre(local2627, screenX + lineX, local1325 + lineY + screenY, local2749, 0xFF000000);
                    Toolkit.active.KA(screenX, screenY, screenX + screenWidth, screenY + screenHeight);
                }
            } else {
                Fonts.b12.renderCentre(local2627, lineX + screenX, screenY + lineY, 0xFFFFFF00, 0xFF000000);
                local409 -= local321 >> 1;
                local1274 -= local321 >> 1;
            }

            OrthoMode.method8927(local409, local1274 + 1, local412, local2654 + 1);
        }
    }

    private OverlayManager() {
        /* empty */
    }
}
