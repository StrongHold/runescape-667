import com.jagex.graphics.Ground;
import com.jagex.graphics.PointLight;
import com.jagex.graphics.Shadow;
import com.jagex.graphics.TextureMetrics;
import com.jagex.math.ColourUtils;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

@OriginalClass("client!qs")
public final class JavaGround extends Ground {

    @OriginalMember(owner = "client!qs", name = "H", descriptor = "F")
    public float cameraE1_3;

    @OriginalMember(owner = "client!qs", name = "Q", descriptor = "F")
    public float cameraE2_2;

    @OriginalMember(owner = "client!qs", name = "R", descriptor = "F")
    public float cameraE3_1;

    @OriginalMember(owner = "client!qs", name = "L", descriptor = "F")
    public float cameraE3_3;

    @OriginalMember(owner = "client!qs", name = "S", descriptor = "F")
    public float cameraE2_3;

    @OriginalMember(owner = "client!qs", name = "M", descriptor = "[[Lclient!fg;")
    public JavaSimpleTile[][] simpleTiles;

    @OriginalMember(owner = "client!qs", name = "F", descriptor = "F")
    public float cameraE1_2;

    @OriginalMember(owner = "client!qs", name = "P", descriptor = "[[Lclient!qh;")
    public JavaSimpleBlendedTile[][] simpleBlendedTiles;

    @OriginalMember(owner = "client!qs", name = "O", descriptor = "F")
    public float cameraE2_1;

    @OriginalMember(owner = "client!qs", name = "J", descriptor = "[[Lclient!vg;")
    public JavaComplexBlendedTile[][] complexBlendedTiles;

    @OriginalMember(owner = "client!qs", name = "A", descriptor = "F")
    public float cameraTy;

    @OriginalMember(owner = "client!qs", name = "T", descriptor = "F")
    public float cameraE1_1;

    @OriginalMember(owner = "client!qs", name = "U", descriptor = "F")
    public float cameraTx;

    @OriginalMember(owner = "client!qs", name = "W", descriptor = "F")
    public float cameraE3_2;

    @OriginalMember(owner = "client!qs", name = "K", descriptor = "[[Lclient!rh;")
    public JavaGenericBlendedTile[][] genericBlendedTiles;

    @OriginalMember(owner = "client!qs", name = "E", descriptor = "[[Lclient!em;")
    public JavaComplexTile[][] complexTiles;

    @OriginalMember(owner = "client!qs", name = "G", descriptor = "F")
    public float cameraTz;

    @OriginalMember(owner = "client!qs", name = "V", descriptor = "I")
    public int depthOverride = -1;

    @OriginalMember(owner = "client!qs", name = "I", descriptor = "Lclient!iaa;")
    public final JavaToolkit toolkit;

    @OriginalMember(owner = "client!qs", name = "D", descriptor = "I")
    public final int featureFlags;

    @OriginalMember(owner = "client!qs", name = "N", descriptor = "[[B")
    public byte[][] lightLevels;

    @OriginalMember(owner = "client!qs", name = "B", descriptor = "[[B")
    public byte[][] shadowLevels;

    @OriginalMember(owner = "client!qs", name = "<init>", descriptor = "(Lclient!iaa;IIII[[I[[II)V")
    public JavaGround(@OriginalArg(0) JavaToolkit toolkit, @OriginalArg(1) int groundFlags, @OriginalArg(2) int featureFlags, @OriginalArg(3) int width, @OriginalArg(4) int length, @OriginalArg(5) int[][] tileHeights, @OriginalArg(6) int[][] heights, @OriginalArg(7) int tileSize) {
        super(width, length, tileSize, tileHeights);
        this.toolkit = toolkit;
        this.featureFlags = featureFlags;
        this.lightLevels = new byte[width + 1][length + 1];
        @Pc(29) int local29 = this.toolkit.ambient >> 9;
        for (@Pc(31) int local31 = 1; local31 < length; local31++) {
            for (@Pc(34) int local34 = 1; local34 < width; local34++) {
                @Pc(53) int local53 = heights[local34 + 1][local31] - heights[local34 - 1][local31];
                @Pc(69) int local69 = heights[local34][local31 + 1] - heights[local34][local31 - 1];
                @Pc(84) int local84 = (int) Math.sqrt(local53 * local53 + tileSize * 512 + local69 * local69);
                @Pc(90) int local90 = (local53 << 8) / local84;
                @Pc(96) int local96 = tileSize * -512 / local84;
                @Pc(102) int local102 = (local69 << 8) / local84;
                @Pc(124) int local124 = local29 + (this.toolkit.sunX * local90 + this.toolkit.sunY * local96 + this.toolkit.sunZ * local102 >> 17);
                local124 >>= 0x1;
                if (local124 < 2) {
                    local124 = 2;
                } else if (local124 > 126) {
                    local124 = 126;
                }
                this.lightLevels[local34][local31] = (byte) local124;
            }
        }
        this.shadowLevels = new byte[width + 1][length + 1];
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(III[[ZZI)V")
    @Override
    public void renderTiles(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) int radius, @OriginalArg(3) boolean[][] visibility, @OriginalArg(4) boolean arg4, @OriginalArg(5) int skipFlags) {
        @Pc(3) JavaMatrix local3 = this.toolkit.camera;
        this.depthOverride = -1;
        this.cameraE1_1 = local3.e1_1;
        this.cameraE1_2 = local3.e1_2;
        this.cameraE1_3 = local3.e1_3;
        this.cameraTx = local3.tx;
        this.cameraE2_1 = local3.e2_1;
        this.cameraE2_2 = local3.e2_2;
        this.cameraE2_3 = local3.e2_3;
        this.cameraTy = local3.ty;
        this.cameraE3_1 = local3.e3_1;
        this.cameraE3_2 = local3.e3_2;
        this.cameraE3_3 = local3.e3_3;
        this.cameraTz = local3.tz;
        for (@Pc(56) int local56 = 0; local56 < radius + radius; local56++) {
            for (@Pc(59) int local59 = 0; local59 < radius + radius; local59++) {
                if (visibility[local56][local59]) {
                    @Pc(72) int local72 = x + local56 - radius;
                    @Pc(78) int local78 = z + local59 - radius;
                    if (local72 >= 0 && local72 < super.sizeX && local78 >= 0 && local78 < super.sizeZ) {
                        this.renderTile(local72, local78, skipFlags);
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(Lclient!lca;[I)V")
    @Override
    public void method7868(@OriginalArg(0) PointLight arg0, @OriginalArg(1) int[] arg1) {
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(Lclient!r;IIIIZ)Z")
    @Override
    public boolean method7874(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3) {
        return false;
    }

    @OriginalMember(owner = "client!qs", name = "CA", descriptor = "(Lclient!r;IIIIZ)V")
    @Override
    public void CA(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5) {
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(IIZLclient!wf;Lclient!lb;[I[I[I[II)V")
    public void renderGenericBlendedTile(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) boolean water, @OriginalArg(3) JavaThreadResource resource, @OriginalArg(4) Rasterizer rasterizer, @OriginalArg(5) int[] screenX, @OriginalArg(6) int[] screenY, @OriginalArg(7) int[] depths, @OriginalArg(8) int[] fogLevels, @OriginalArg(9) int skipFlags) {
        @Pc(6) JavaGenericBlendedTile local6 = this.genericBlendedTiles[x][z];
        if (skipFlags != 0 && (skipFlags & 0x2) != 0 || local6 == null) {
            return;
        }
        @Pc(37) int local37;
        @Pc(42) int local42;
        @Pc(52) int local52;
        @Pc(172) float local172;
        @Pc(193) float local193;
        @Pc(73) float local73;
        @Pc(26) int local26;
        @Pc(95) int local95;
        @Pc(115) int local115;
        if (this.depthOverride == -1) {
            for (local26 = 0; local26 < local6.vertexCount; local26++) {
                local37 = local6.verticesX[local26] + (x << super.tileSizeShift);
                local42 = local6.verticesY[local26];
                local52 = local6.verticesZ[local26] + (z << super.tileSizeShift);
                local73 = this.cameraTz + (this.cameraE3_1 * (float) local37 + this.cameraE3_2 * (float) local42 + this.cameraE3_3 * (float) local52);
                if (local73 <= (float) this.toolkit.zNear) {
                    return;
                }
                fogLevels[local26] = 0;
                if (water) {
                    local95 = (int) (local73 - (float) resource.fogPlane);
                    if (local95 > 255) {
                        local95 = 255;
                    }
                    if (local95 > 0) {
                        fogLevels[local26] = local95;
                        local115 = local6.waterDepths[local26] * local95 / 255;
                        if (local115 > 0) {
                            local42 -= local115;
                        }
                    }
                } else if (resource.fogActive) {
                    local95 = (int) (local73 - (float) resource.fogPlane);
                    if (local95 > 0) {
                        fogLevels[local26] = local95;
                        if (fogLevels[local26] > 255) {
                            fogLevels[local26] = 255;
                        }
                    }
                }
                local172 = this.cameraTx + (this.cameraE1_1 * (float) local37 + this.cameraE1_2 * (float) local42 + this.cameraE1_3 * (float) local52);
                local193 = this.cameraTy + (this.cameraE2_1 * (float) local37 + this.cameraE2_2 * (float) local42 + this.cameraE2_3 * (float) local52);
                screenX[local26] = rasterizer.minX + (int) (local172 * (float) this.toolkit.projectionScaleX / local73);
                screenY[local26] = rasterizer.minY + (int) (local193 * (float) this.toolkit.projectionScaleY / local73);
                depths[local26] = (int) local73;
            }
        } else {
            for (local26 = 0; local26 < local6.vertexCount; local26++) {
                local37 = local6.verticesX[local26] + (x << super.tileSizeShift);
                local42 = local6.verticesY[local26];
                local52 = local6.verticesZ[local26] + (z << super.tileSizeShift);
                local73 = this.cameraTz + (this.cameraE3_1 * (float) local37 + this.cameraE3_2 * (float) local42 + this.cameraE3_3 * (float) local52);
                fogLevels[local26] = 0;
                if (water) {
                    local95 = this.depthOverride - resource.fogPlane;
                    if (local95 > 255) {
                        local95 = 255;
                    }
                    if (local95 > 0) {
                        fogLevels[local26] = local95;
                        local115 = local6.waterDepths[local26] * local95 / 255;
                        if (local115 > 0) {
                            local42 -= local115;
                        }
                    }
                } else if (resource.fogActive) {
                    local95 = this.depthOverride - resource.fogPlane;
                    if (local95 > 0) {
                        fogLevels[local26] = local95;
                        if (fogLevels[local26] > 255) {
                            fogLevels[local26] = 255;
                        }
                    }
                }
                local172 = this.cameraTx + (this.cameraE1_1 * (float) local37 + this.cameraE1_2 * (float) local42 + this.cameraE1_3 * (float) local52);
                local193 = this.cameraTy + (this.cameraE2_1 * (float) local37 + this.cameraE2_2 * (float) local42 + this.cameraE2_3 * (float) local52);
                screenX[local26] = rasterizer.minX + (int) (local172 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                screenY[local26] = rasterizer.minY + (int) (local193 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                depths[local26] = (int) local73;
            }
        }
        @Pc(441) float local441 = (float) super.tileSize;
        for (local95 = 0; local95 < local6.faceCount; local95++) {
            local115 = local95 * 3;
            @Pc(452) int local452 = local115 + 1;
            @Pc(456) int local456 = local452 + 1;
            @Pc(460) int local460 = screenX[local115];
            @Pc(464) int local464 = screenX[local452];
            @Pc(468) int local468 = screenX[local456];
            @Pc(472) int local472 = screenY[local115];
            @Pc(476) int local476 = screenY[local452];
            @Pc(480) int local480 = screenY[local456];
            if ((local460 - local464) * (local480 - local476) - (local472 - local476) * (local468 - local464) > 0) {
                rasterizer.clamp = local460 < 0 || local464 < 0 || local468 < 0 || local460 > rasterizer.width || local464 > rasterizer.width || local468 > rasterizer.width;
                if (fogLevels[local115] + fogLevels[local452] + fogLevels[local456] < 765) {
                    @Pc(550) int local550 = x << super.tileSizeShift;
                    @Pc(555) int local555 = z << super.tileSizeShift;
                    if ((local6.vertexColours[local115] & 0xFFFFFF) != 0) {
                        if (local6.vertexTextures[local115] == local6.vertexTextures[local452] && local6.vertexTextures[local115] == local6.vertexTextures[local456] && local6.vertexSizes[local115] == local6.vertexSizes[local452] && local6.vertexSizes[local115] == local6.vertexSizes[local456]) {
                            rasterizer.renderTexturedTriangleRgb((float) local472, (float) local476, (float) local480, (float) local460, (float) local464, (float) local468, (float) depths[local115], (float) depths[local452], (float) depths[local456], (float) (local550 + local6.verticesX[local115]) / (float) local6.vertexSizes[local115], (float) (local550 + local6.verticesX[local452]) / (float) local6.vertexSizes[local452], (float) (local550 + local6.verticesX[local456]) / (float) local6.vertexSizes[local456], (float) (local555 + local6.verticesZ[local115]) / (float) local6.vertexSizes[local115], (float) (local555 + local6.verticesZ[local452]) / (float) local6.vertexSizes[local452], (float) (local555 + local6.verticesZ[local456]) / (float) local6.vertexSizes[local456], local6.vertexColours[local115], local6.vertexColours[local452], local6.vertexColours[local456], resource.fogColour, fogLevels[local115], fogLevels[local452], fogLevels[local456], local6.vertexTextures[local115]);
                        } else {
                            rasterizer.renderBlendedTexturedTriangle((float) local472, (float) local476, (float) local480, (float) local460, (float) local464, (float) local468, (float) depths[local115], (float) depths[local452], (float) depths[local456], (float) (local550 + local6.verticesX[local115]) / local441, (float) (local550 + local6.verticesX[local452]) / local441, (float) (local550 + local6.verticesX[local456]) / local441, (float) (local555 + local6.verticesZ[local115]) / local441, (float) (local555 + local6.verticesZ[local452]) / local441, (float) (local555 + local6.verticesZ[local456]) / local441, local6.vertexColours[local115], local6.vertexColours[local452], local6.vertexColours[local456], resource.fogColour, fogLevels[local115], fogLevels[local452], fogLevels[local456], local6.vertexTextures[local115], local441 / (float) local6.vertexSizes[local115], local6.vertexTextures[local452], local441 / (float) local6.vertexSizes[local452], local6.vertexTextures[local456], local441 / (float) local6.vertexSizes[local456]);
                        }
                    }
                } else {
                    rasterizer.renderFlatTriangleRgb((float) local472, (float) local476, (float) local480, (float) local460, (float) local464, (float) local468, (float) depths[local115], (float) depths[local452], (float) depths[local456], resource.fogColour);
                }
            }
        }
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(II)V")
    @Override
    public void renderTile(@OriginalArg(0) int x, @OriginalArg(1) int z) {
        this.renderTile(x, z, 0);
    }

    @OriginalMember(owner = "client!qs", name = "YA", descriptor = "()V")
    @Override
    public void YA() {
        this.lightLevels = null;
        this.shadowLevels = null;
    }

    @OriginalMember(owner = "client!qs", name = "U", descriptor = "(II[I[I[I[I[I[I[I[IIIIZ)V")
    @Override
    public void U(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) int[] offsetX, @OriginalArg(3) int[] offsetLevel, @OriginalArg(4) int[] offsetY, @OriginalArg(5) int[] waterDepths, @OriginalArg(6) int[] blendedColours, @OriginalArg(7) int[] overlayBlendColours, @OriginalArg(8) int[] blendedTextures, @OriginalArg(9) int[] blendedSizes, @OriginalArg(10) int waterColour, @OriginalArg(11) int waterDepth, @OriginalArg(12) int waterBias, @OriginalArg(13) boolean allowShadow) {
        @Pc(9) boolean allTexturesEnabled = (this.featureFlags & 0x20) == 0;

        if (this.simpleBlendedTiles == null && !allTexturesEnabled) {
            this.simpleBlendedTiles = new JavaSimpleBlendedTile[super.sizeX][super.sizeZ];
            this.complexBlendedTiles = new JavaComplexBlendedTile[super.sizeX][super.sizeZ];
        } else if (this.genericBlendedTiles == null && allTexturesEnabled) {
            this.genericBlendedTiles = new JavaGenericBlendedTile[super.sizeX][super.sizeZ];
        } else if (this.simpleTiles != null) {
            throw new IllegalStateException();
        }

        if (offsetX == null || offsetX.length == 0) {
            return;
        }

        for (@Pc(67) int local67 = 0; local67 < blendedColours.length; local67++) {
            if (blendedColours[local67] == -1) {
                blendedColours[local67] = 0;
            } else {
                blendedColours[local67] = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(blendedColours[local67]) & 0xFFFF] << 8 | 0xFF;
            }
        }

        if (overlayBlendColours != null) {
            for (@Pc(106) int local106 = 0; local106 < overlayBlendColours.length; local106++) {
                if (overlayBlendColours[local106] == -1) {
                    overlayBlendColours[local106] = 0;
                } else {
                    overlayBlendColours[local106] = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(overlayBlendColours[local106]) & 0xFFFF] << 8 | 0xFF;
                }
            }
        }

        @Pc(205) int local205;
        @Pc(210) int local210;
        @Pc(214) int local214;
        @Pc(236) int local236;
        @Pc(363) int local363;
        @Pc(410) int local410;
        @Pc(498) int local498;
        @Pc(530) int local530;
        if (allTexturesEnabled) {
            @Pc(147) JavaGenericBlendedTile tile = new JavaGenericBlendedTile();
            tile.vertexCount = (short) offsetX.length;
            tile.faceCount = (short) (offsetX.length / 3);
            tile.verticesX = new short[tile.vertexCount];
            tile.verticesY = new short[tile.vertexCount];
            tile.verticesZ = new short[tile.vertexCount];
            tile.vertexColours = new int[tile.vertexCount];
            tile.vertexTextures = new short[tile.vertexCount];
            tile.vertexSizes = new short[tile.vertexCount];
            tile.vertexLight = new byte[tile.vertexCount];

            if (waterDepths != null) {
                tile.waterDepths = new short[tile.vertexCount];
            }

            for (local205 = 0; local205 < tile.vertexCount; local205++) {
                local210 = offsetX[local205];
                local214 = offsetY[local205];
                if (local210 == 0 && local214 == 0) {
                    local236 = this.lightLevels[x][z] - this.shadowLevels[x][z];
                } else if (local210 == 0 && local214 == super.tileSize) {
                    local236 = this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1];
                } else if (local210 == super.tileSize && local214 == super.tileSize) {
                    local236 = this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1];
                } else if (local210 == super.tileSize && local214 == 0) {
                    local236 = this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z];
                } else {
                    local363 = (this.lightLevels[x][z] - this.shadowLevels[x][z]) * (super.tileSize - local210) + (this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z]) * local210;
                    local410 = (this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1]) * (super.tileSize - local210) + (this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1]) * local210;
                    local236 = local363 * (super.tileSize - local214) + local410 * local214 >> super.tileSizeShift * 2;
                }

                local363 = (x << super.tileSizeShift) + local210;
                local410 = (z << super.tileSizeShift) + local214;

                tile.verticesX[local205] = (short) local210;
                tile.verticesZ[local205] = (short) local214;
                tile.verticesY[local205] = (short) (this.averageHeight(local363, local410) + (offsetLevel == null ? 0 : offsetLevel[local205]));

                if (local236 < 0) {
                    local236 = 0;
                }

                if (blendedColours[local205] == 0) {
                    tile.vertexColours[local205] = 0;
                    if (overlayBlendColours != null) {
                        tile.vertexLight[local205] = (byte) local236;
                    }
                } else {
                    local498 = 0;
                    if (waterDepths != null) {
                        @Pc(510) short local510 = tile.waterDepths[local205] = (short) waterDepths[local205];
                        if (waterDepth != 0) {
                            local498 = local510 * 255 / waterDepth;
                            if (local498 < 0) {
                                local498 = 0;
                            } else if (local498 > 255) {
                                local498 = 255;
                            }
                        }
                    }

                    local530 = -16777216;
                    if (blendedTextures[local205] != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(blendedTextures[local205]).effectType)) {
                        local530 = -1694498816;
                    }

                    tile.vertexColours[local205] = local530 | Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local205] >> 8, local236), local498);
                    if (overlayBlendColours != null) {
                        tile.vertexLight[local205] = (byte) local236;
                    }
                }

                tile.vertexTextures[local205] = (short) blendedTextures[local205];
                tile.vertexSizes[local205] = (short) blendedSizes[local205];
            }

            if (overlayBlendColours != null) {
                tile.faceBlendedColours = new int[tile.faceCount];
            }

            for (local210 = 0; local210 < tile.faceCount; local210++) {
                local214 = local210 * 3;

                if (overlayBlendColours != null && overlayBlendColours[local214] != 0) {
                    tile.faceBlendedColours[local210] = overlayBlendColours[local214] >> 8 | 0xFF000000;
                }
            }

            this.genericBlendedTiles[x][z] = tile;
            return;
        }

        @Pc(654) boolean local654 = true;
        local205 = -1;
        local210 = -1;
        local214 = -1;
        local236 = -1;

        if (offsetX.length == 6) {
            for (local363 = 0; local363 < 6; local363++) {
                if (offsetX[local363] == 0 && offsetY[local363] == 0) {
                    if (local205 != -1 && blendedColours[local205] != blendedColours[local363]) {
                        local654 = false;
                        break;
                    }

                    local205 = local363;
                } else if (offsetX[local363] == super.tileSize && offsetY[local363] == 0) {
                    if (local210 != -1 && blendedColours[local210] != blendedColours[local363]) {
                        local654 = false;
                        break;
                    }

                    local210 = local363;
                } else if (offsetX[local363] == super.tileSize && offsetY[local363] == super.tileSize) {
                    if (local214 != -1 && blendedColours[local214] != blendedColours[local363]) {
                        local654 = false;
                        break;
                    }

                    local214 = local363;
                } else if (offsetX[local363] == 0 && offsetY[local363] == super.tileSize) {
                    if (local236 != -1 && blendedColours[local236] != blendedColours[local363]) {
                        local654 = false;
                        break;
                    }

                    local236 = local363;
                }
            }

            if (local205 == -1 || local210 == -1 || local214 == -1 || local236 == -1) {
                local654 = false;
            }

            if (local654) {
                if (offsetLevel != null) {
                    for (local410 = 0; local410 < 4; local410++) {
                        if (offsetLevel[local410] != 0) {
                            local654 = false;
                            break;
                        }
                    }
                }

                if (local654) {
                    for (local410 = 1; local410 < 4; local410++) {
                        if (offsetX[local410] != offsetX[0] && offsetX[local410] != offsetX[0] + super.tileSize && offsetX[local410] != offsetX[0] - super.tileSize) {
                            local654 = false;
                            break;
                        }

                        if (offsetY[local410] != offsetY[0] && offsetY[local410] != offsetY[0] + super.tileSize && offsetY[local410] != offsetY[0] - super.tileSize) {
                            local654 = false;
                            break;
                        }
                    }
                }
            }
        } else {
            local654 = false;
        }

        if (!local654) {
            @Pc(1760) JavaComplexBlendedTile local1760 = new JavaComplexBlendedTile();
            local1760.vertexCount = (short) offsetX.length;
            local1760.faceCount = (short) (offsetX.length / 3);
            local1760.verticesX = new short[local1760.vertexCount];
            local1760.verticesY = new short[local1760.vertexCount];
            local1760.verticesZ = new short[local1760.vertexCount];
            local1760.vertexColours = new int[local1760.vertexCount];
            if (waterDepths != null) {
                local1760.waterDepths = new short[local1760.vertexCount];
            }

            @Pc(1834) int local1834;
            @Pc(1961) int local1961;
            @Pc(2008) int local2008;
            @Pc(2098) int local2098;
            for (local410 = 0; local410 < local1760.vertexCount; local410++) {
                local498 = offsetX[local410];
                local530 = offsetY[local410];

                if (local498 == 0 && local530 == 0) {
                    local1834 = this.lightLevels[x][z] - this.shadowLevels[x][z];
                } else if (local498 == 0 && local530 == super.tileSize) {
                    local1834 = this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1];
                } else if (local498 == super.tileSize && local530 == super.tileSize) {
                    local1834 = this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1];
                } else if (local498 == super.tileSize && local530 == 0) {
                    local1834 = this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z];
                } else {
                    local1961 = (this.lightLevels[x][z] - this.shadowLevels[x][z]) * (super.tileSize - local498) + (this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z]) * local498;
                    local2008 = (this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1]) * (super.tileSize - local498) + (this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1]) * local498;
                    local1834 = local1961 * (super.tileSize - local530) + local2008 * local530 >> super.tileSizeShift * 2;
                }

                local1961 = (x << super.tileSizeShift) + local498;
                local2008 = (z << super.tileSizeShift) + local530;
                local1760.verticesX[local410] = (short) local498;
                local1760.verticesZ[local410] = (short) local530;
                local1760.verticesY[local410] = (short) (this.averageHeight(local1961, local2008) + (offsetLevel == null ? 0 : offsetLevel[local410]));
                if (local1834 < 0) {
                    local1834 = 0;
                }

                if (blendedColours[local410] != 0) {
                    local2098 = 0;
                    if (waterDepths != null) {
                        @Pc(2110) short local2110 = local1760.waterDepths[local410] = (short) waterDepths[local410];
                        if (waterDepth != 0) {
                            local2098 = local2110 * 255 / waterDepth;
                            if (local2098 < 0) {
                                local2098 = 0;
                            } else if (local2098 > 255) {
                                local2098 = 255;
                            }
                        }
                    }

                    local1760.vertexColours[local410] = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local410] >> 8, local1834), local2098);
                    if (overlayBlendColours != null) {
                        local1760.vertexColours[local410] |= local1834 << 25;
                    }
                } else if (overlayBlendColours == null) {
                    local1760.vertexColours[local410] = 0;
                } else {
                    local1760.vertexColours[local410] = local1834 << 25;
                }
            }

            @Pc(2164) boolean local2164 = false;
            for (local530 = 0; local530 < local1760.faceCount; local530++) {
                if (blendedTextures[local530 * 3] != -1 && !this.toolkit.textureSource.getMetrics(blendedTextures[local530 * 3]).disableable) {
                    local2164 = true;
                }
            }

            if (overlayBlendColours != null) {
                local1760.faceBlendedColours = new int[local1760.faceCount];
            }

            if (local2164) {
                local1760.faceTextures = new short[local1760.faceCount];
                local1760.faceSizes = new short[local1760.faceCount];
            }

            for (local1834 = 0; local1834 < local1760.faceCount; local1834++) {
                local1961 = local1834 * 3;
                if (overlayBlendColours != null && overlayBlendColours[local1961] != 0) {
                    local1760.faceBlendedColours[local1834] = overlayBlendColours[local1961] >> 8;
                }

                if (local2164) {
                    local2008 = local1961 + 1;
                    local2098 = local2008 + 1;
                    @Pc(2258) boolean local2258 = false;
                    @Pc(2260) boolean local2260 = true;
                    @Pc(2264) int local2264 = blendedTextures[local1961];
                    if (local2264 == -1 || this.toolkit.textureSource.getMetrics(local2264).disableable) {
                        local2260 = false;
                    } else {
                        local2258 = true;
                    }

                    local2264 = blendedTextures[local2008];
                    if (local2264 == -1 || this.toolkit.textureSource.getMetrics(local2264).disableable) {
                        local2260 = false;
                    } else {
                        local2258 = true;
                    }

                    local2264 = blendedTextures[local2098];
                    if (local2264 == -1 || this.toolkit.textureSource.getMetrics(local2264).disableable) {
                        local2260 = false;
                    } else {
                        local2258 = true;
                    }

                    if (local2260) {
                        local1760.faceTextures[local1834] = (short) local2264;
                        local1760.faceSizes[local1834] = (short) blendedSizes[local1961];
                    } else {
                        if (local2258) {
                            local2264 = blendedTextures[local1961];
                            if (local2264 != -1 && !this.toolkit.textureSource.getMetrics(local2264).disableable) {
                                local1760.vertexColours[local1961] = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.toolkit.textureSource.getMetrics(local2264).aShort37 & 0xFFFF) & 0xFFFF];
                            }
                            local2264 = blendedTextures[local2008];
                            if (local2264 != -1 && !this.toolkit.textureSource.getMetrics(local2264).disableable) {
                                local1760.vertexColours[local2008] = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.toolkit.textureSource.getMetrics(local2264).aShort37 & 0xFFFF) & 0xFFFF];
                            }
                            local2264 = blendedTextures[local2098];
                            if (local2264 != -1 && !this.toolkit.textureSource.getMetrics(local2264).disableable) {
                                local1760.vertexColours[local2098] = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(this.toolkit.textureSource.getMetrics(local2264).aShort37 & 0xFFFF) & 0xFFFF];
                            }
                        }
                        local1760.faceTextures[local1834] = -1;
                    }
                }
            }

            this.complexBlendedTiles[x][z] = local1760;
            return;
        }

        @Pc(931) JavaSimpleBlendedTile local931 = new JavaSimpleBlendedTile();
        local410 = blendedColours[0];
        local498 = blendedTextures[0];

        if (overlayBlendColours != null) {
            local931.blendedColour = overlayBlendColours[0] >> 8;

            if (local410 == 0) {
                local931.flags = (byte) (local931.flags | 0x2);
            }
        } else if (local410 == 0) {
            return;
        }

        if (super.tileHeights[x][z] == super.tileHeights[x + 1][z] && super.tileHeights[x][z] == super.tileHeights[x + 1][z + 1] && super.tileHeights[x][z] == super.tileHeights[x][z + 1]) {
            local931.flags = (byte) (local931.flags | 0x1);
        }

        if (local498 == -1 || (local931.flags & 0x2) != 0 || this.toolkit.textureSource.getMetrics(local498).disableable) {
            if (waterDepths == null || waterDepth == 0) {
                local530 = 0;
            } else {
                local530 = waterDepths[local205] * 255 / waterDepth;

                if (local530 < 0) {
                    local530 = 0;
                } else if (local530 > 255) {
                    local530 = 255;
                }
            }

            local931.colourSw = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local205] >> 8, this.lightLevels[x][z] - this.shadowLevels[x][z]), local530);
            if (local931.blendedColour != 0) {
                local931.colourSw |= this.shadowLevels[x][z] + 255 - this.lightLevels[x][z] << 25;
            }

            if (waterDepths == null || waterDepth == 0) {
                local530 = 0;
            } else {
                local530 = waterDepths[local210] * 255 / waterDepth;

                if (local530 < 0) {
                    local530 = 0;
                } else if (local530 > 255) {
                    local530 = 255;
                }
            }

            local931.colourSe = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local210] >> 8, this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z]), local530);
            if (local931.blendedColour != 0) {
                local931.colourSe |= this.shadowLevels[x + 1][z] + 255 - this.lightLevels[x + 1][z] << 25;
            }

            if (waterDepths == null || waterDepth == 0) {
                local530 = 0;
            } else {
                local530 = waterDepths[local214] * 255 / waterDepth;

                if (local530 < 0) {
                    local530 = 0;
                } else if (local530 > 255) {
                    local530 = 255;
                }
            }

            local931.colourNe = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local214] >> 8, this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1]), local530);
            if (local931.blendedColour != 0) {
                local931.colourNe |= this.shadowLevels[x + 1][z + 1] + 255 - this.lightLevels[x + 1][z + 1] << 25;
            }

            if (waterDepths == null || waterDepth == 0) {
                local530 = 0;
            } else {
                local530 = waterDepths[local236] * 255 / waterDepth;
                if (local530 < 0) {
                    local530 = 0;
                } else if (local530 > 255) {
                    local530 = 255;
                }
            }

            local931.colourNw = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local236] >> 8, this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1]), local530);
            if (local931.blendedColour != 0) {
                local931.colourNw |= this.shadowLevels[x][z + 1] + 255 - this.lightLevels[x][z + 1] << 25;
            }

            local931.texture = -1;
        } else {
            if (waterDepths == null || waterDepth == 0) {
                local530 = 0;
            } else {
                local530 = waterDepths[local205] * 255 / waterDepth;

                if (local530 < 0) {
                    local530 = 0;
                } else if (local530 > 255) {
                    local530 = 255;
                }
            }

            local931.colourSw = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local205] >> 8, this.lightLevels[x][z] - this.shadowLevels[x][z]), local530);
            if (local931.blendedColour != 0) {
                local931.colourSw |= this.shadowLevels[x][z] + 255 - this.lightLevels[x][z] << 25;
            }

            if (waterDepths == null || waterDepth == 0) {
                local530 = 0;
            } else {
                local530 = waterDepths[local210] * 255 / waterDepth;
                if (local530 < 0) {
                    local530 = 0;
                } else if (local530 > 255) {
                    local530 = 255;
                }
            }

            local931.colourSe = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local210] >> 8, this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z]), local530);
            if (local931.blendedColour != 0) {
                local931.colourSe |= this.shadowLevels[x + 1][z] + 255 - this.lightLevels[x + 1][z] << 25;
            }

            if (waterDepths == null || waterDepth == 0) {
                local530 = 0;
            } else {
                local530 = waterDepths[local214] * 255 / waterDepth;
                if (local530 < 0) {
                    local530 = 0;
                } else if (local530 > 255) {
                    local530 = 255;
                }
            }

            local931.colourNe = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local214] >> 8, this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1]), local530);
            if (local931.blendedColour != 0) {
                local931.colourNe |= this.shadowLevels[x + 1][z + 1] + 255 - this.lightLevels[x + 1][z + 1] << 25;
            }

            if (waterDepths == null || waterDepth == 0) {
                local530 = 0;
            } else {
                local530 = waterDepths[local236] * 255 / waterDepth;

                if (local530 < 0) {
                    local530 = 0;
                } else if (local530 > 255) {
                    local530 = 255;
                }
            }

            local931.colourNw = Static572.lerpRgb(waterColour, Static732.scaleRgb(blendedColours[local236] >> 8, this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1]), local530);
            local931.texture = (short) local498;
        }

        if (waterDepths != null) {
            local931.waterDepthNe = (short) waterDepths[local214];
            local931.waterDepthNw = (short) waterDepths[local236];
            local931.waterDepthSe = (short) waterDepths[local210];
            local931.waterDepthSw = (short) waterDepths[local205];
        }

        this.simpleBlendedTiles[x][z] = local931;
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(I)Z")
    public boolean isWaterEffect(@OriginalArg(0) int effectType) {
        if ((this.featureFlags & 0x8) == 0) {
            return false;
        } else if (effectType == 4) {
            return true;
        } else if (effectType == 8) {
            return true;
        } else {
            return effectType == 9;
        }
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(IIIIIII[[ZLclient!wf;Lclient!lb;[I[I)V")
    public void drawUnblendedMinimapTiles(@OriginalArg(3) int x1, @OriginalArg(4) int z1, @OriginalArg(5) int x2, @OriginalArg(6) int z2, @OriginalArg(7) boolean[][] visibility, @OriginalArg(8) JavaThreadResource resource, @OriginalArg(9) Rasterizer rasterizer, @OriginalArg(10) int[] screenX, @OriginalArg(11) int[] screenY) {
        @Pc(7) int local7 = (z2 - z1) * 1024 / 256;
        @Pc(14) boolean local14 = resource.zWrite;
        this.toolkit.C(false);
        rasterizer.fastScanline = false;
        rasterizer.halfBlend = false;
        @Pc(26) int local26 = 0;
        @Pc(30) int local30 = local7;
        for (@Pc(32) int local32 = x1; local32 < x2; local32++) {
            for (@Pc(35) int local35 = z1; local35 < z2; local35++) {
                if (visibility[local32 - x1][local35 - z1]) {
                    @Pc(85) int local85;
                    if (this.simpleTiles[local32][local35] != null) {
                        @Pc(62) JavaSimpleTile local62 = this.simpleTiles[local32][local35];
                        if (local62.texture != -1 && (local62.flags & 0x2) == 0 && local62.blendedColour == -1) {
                            local85 = this.toolkit.textureHsl(local62.texture);
                            rasterizer.renderTriangleHslRgb((float) (local30 - 4), (float) (local30 - 4), (float) local30, (float) (local26 + 4), (float) local26, (float) (local26 + 4), 100.0F, 100.0F, 100.0F, (float) Static244.scaleHslLightness(local62.hslNe & 0xFFFF, local85), (float) Static244.scaleHslLightness(local62.hslNw & 0xFFFF, local85), (float) Static244.scaleHslLightness(local62.hslSe & 0xFFFF, local85));
                            rasterizer.renderTriangleHslRgb((float) local30, (float) local30, (float) (local30 - 4), (float) local26, (float) (local26 + 4), (float) local26, 100.0F, 100.0F, 100.0F, (float) Static244.scaleHslLightness(local62.hslSw & 0xFFFF, local85), (float) Static244.scaleHslLightness(local62.hslSe & 0xFFFF, local85), (float) Static244.scaleHslLightness(local62.hslNw & 0xFFFF, local85));
                        } else if (local62.blendedColour == -1) {
                            rasterizer.renderTriangleHslRgb((float) (local30 - 4), (float) (local30 - 4), (float) local30, (float) (local26 + 4), (float) local26, (float) (local26 + 4), 100.0F, 100.0F, 100.0F, (float) (local62.hslNe & 0xFFFF), (float) (local62.hslNw & 0xFFFF), (float) (local62.hslSe & 0xFFFF));
                            rasterizer.renderTriangleHslRgb((float) local30, (float) local30, (float) (local30 - 4), (float) local26, (float) (local26 + 4), (float) local26, 100.0F, 100.0F, 100.0F, (float) (local62.hslSw & 0xFFFF), (float) (local62.hslSe & 0xFFFF), (float) (local62.hslNw & 0xFFFF));
                        } else {
                            local85 = local62.blendedColour;
                            rasterizer.renderTriangleHslRgb((float) (local30 - 4), (float) (local30 - 4), (float) local30, (float) (local26 + 4), (float) local26, (float) (local26 + 4), 100.0F, 100.0F, 100.0F, (float) local85, (float) local85, (float) local85);
                            rasterizer.renderTriangleHslRgb((float) local30, (float) local30, (float) (local30 - 4), (float) local26, (float) (local26 + 4), (float) local26, 100.0F, 100.0F, 100.0F, (float) local85, (float) local85, (float) local85);
                        }
                    } else if (this.complexTiles[local32][local35] != null) {
                        @Pc(338) JavaComplexTile local338 = this.complexTiles[local32][local35];
                        for (local85 = 0; local85 < local338.vertexCount; local85++) {
                            screenX[local85] = local26 + local338.verticesX[local85] * 4 / super.tileSize;
                            screenY[local85] = local30 - local338.verticesZ[local85] * 4 / super.tileSize;
                        }
                        for (@Pc(376) int local376 = 0; local376 < local338.faceCount; local376++) {
                            @Pc(382) short local382 = local338.faceA[local376];
                            @Pc(387) short local387 = local338.faceB[local376];
                            @Pc(392) short local392 = local338.faceC[local376];
                            @Pc(396) int local396 = screenX[local382];
                            @Pc(400) int local400 = screenX[local387];
                            @Pc(404) int local404 = screenX[local392];
                            @Pc(408) int local408 = screenY[local382];
                            @Pc(412) int local412 = screenY[local387];
                            @Pc(416) int local416 = screenY[local392];
                            @Pc(432) int local432;
                            if (local338.faceBlendedColours != null && local338.faceBlendedColours[local376] != -1) {
                                local432 = local338.faceBlendedColours[local376];
                                rasterizer.renderTriangleHslRgb((float) local408, (float) local412, (float) local416, (float) local396, (float) local400, (float) local404, 100.0F, 100.0F, 100.0F, (float) Static244.scaleHslLightness(local338.verticesLight[local382], local432), (float) Static244.scaleHslLightness(local338.verticesLight[local387], local432), (float) Static244.scaleHslLightness(local338.verticesLight[local392], local432));
                            } else if (local338.faceTextures == null || local338.faceTextures[local376] == -1) {
                                local432 = local338.faceColours[local376];
                                rasterizer.renderTriangleHslRgb((float) local408, (float) local412, (float) local416, (float) local396, (float) local400, (float) local404, 100.0F, 100.0F, 100.0F, (float) Static244.scaleHslLightness(local338.verticesLight[local382], local432), (float) Static244.scaleHslLightness(local338.verticesLight[local387], local432), (float) Static244.scaleHslLightness(local338.verticesLight[local392], local432));
                            } else {
                                local432 = this.toolkit.textureHsl(local338.faceTextures[local376]);
                                rasterizer.renderTriangleHslRgb((float) local408, (float) local412, (float) local416, (float) local396, (float) local400, (float) local404, 100.0F, 100.0F, 100.0F, (float) Static244.scaleHslLightness(local338.verticesLight[local382], local432), (float) Static244.scaleHslLightness(local338.verticesLight[local387], local432), (float) Static244.scaleHslLightness(local338.verticesLight[local392], local432));
                            }
                        }
                    }
                }
                local30 -= 4;
            }
            local30 = local7;
            local26 += 4;
        }
        rasterizer.fastScanline = true;
        this.toolkit.C(local14);
    }

    @OriginalMember(owner = "client!qs", name = "b", descriptor = "(IIIIIII[[ZLclient!wf;Lclient!lb;[I[I)V")
    public void drawBlendedMinimapTiles(@OriginalArg(3) int x1, @OriginalArg(4) int z1, @OriginalArg(5) int x2, @OriginalArg(6) int z2, @OriginalArg(7) boolean[][] visibility, @OriginalArg(8) JavaThreadResource resource, @OriginalArg(9) Rasterizer rasterizer, @OriginalArg(10) int[] screenX, @OriginalArg(11) int[] screenY) {
        @Pc(7) int local7 = (z2 - z1) * 1024 / 256;
        @Pc(14) boolean local14 = resource.zWrite;
        this.toolkit.C(false);
        rasterizer.fastScanline = false;
        rasterizer.halfBlend = false;
        @Pc(26) int local26 = 0;
        @Pc(30) int local30 = local7;
        for (@Pc(32) int local32 = x1; local32 < x2; local32++) {
            for (@Pc(35) int local35 = z1; local35 < z2; local35++) {
                if (visibility[local32 - x1][local35 - z1]) {
                    @Pc(89) int local89;
                    @Pc(379) int local379;
                    @Pc(384) int local384;
                    @Pc(388) int local388;
                    @Pc(392) int local392;
                    @Pc(396) int local396;
                    @Pc(400) int local400;
                    @Pc(404) int local404;
                    @Pc(408) int local408;
                    @Pc(412) int local412;
                    @Pc(416) int local416;
                    @Pc(449) int local449;
                    if (this.simpleBlendedTiles == null) {
                        if (this.genericBlendedTiles[local32][local35] != null) {
                            @Pc(593) JavaGenericBlendedTile local593 = this.genericBlendedTiles[local32][local35];
                            for (local89 = 0; local89 < local593.vertexCount; local89++) {
                                screenX[local89] = local26 + local593.verticesX[local89] * 4 / super.tileSize;
                                screenY[local89] = local30 - local593.verticesZ[local89] * 4 / super.tileSize;
                            }
                            for (local379 = 0; local379 < local593.faceCount; local379++) {
                                local384 = local379 * 3;
                                local388 = local384 + 1;
                                local392 = local388 + 1;
                                local396 = screenX[local384];
                                local400 = screenX[local388];
                                local404 = screenX[local392];
                                local408 = screenY[local384];
                                local412 = screenY[local388];
                                local416 = screenY[local392];
                                if (local593.faceBlendedColours == null || local593.faceBlendedColours[local379] == 0) {
                                    rasterizer.renderTriangleRgb((float) local408, (float) local412, (float) local416, (float) local396, (float) local400, (float) local404, 100.0F, 100.0F, 100.0F, local593.vertexColours[local384], local593.vertexColours[local388], local593.vertexColours[local392]);
                                } else {
                                    local449 = local593.faceBlendedColours[local379];
                                    rasterizer.renderTriangleRgb((float) local408, (float) local412, (float) local416, (float) local396, (float) local400, (float) local404, 100.0F, 100.0F, 100.0F, local449, local449, local449);
                                }
                            }
                        }
                    } else if (this.simpleBlendedTiles[local32][local35] != null) {
                        @Pc(67) JavaSimpleBlendedTile local67 = this.simpleBlendedTiles[local32][local35];
                        if (local67.texture != -1 && (local67.flags & 0x2) == 0 && local67.blendedColour == 0) {
                            local89 = this.toolkit.textureHsl(local67.texture);
                            rasterizer.renderTriangleHslRgb((float) (local30 - 4), (float) (local30 - 4), (float) local30, (float) (local26 + 4), (float) local26, (float) (local26 + 4), 100.0F, 100.0F, 100.0F, (float) Static244.scaleHslLightness(local67.colourNe, local89), (float) Static244.scaleHslLightness(local67.colourNw, local89), (float) Static244.scaleHslLightness(local67.colourSe, local89));
                            rasterizer.renderTriangleHslRgb((float) local30, (float) local30, (float) (local30 - 4), (float) local26, (float) (local26 + 4), (float) local26, 100.0F, 100.0F, 100.0F, (float) Static244.scaleHslLightness(local67.colourSw, local89), (float) Static244.scaleHslLightness(local67.colourSe, local89), (float) Static244.scaleHslLightness(local67.colourNw, local89));
                        } else if (local67.blendedColour == 0) {
                            rasterizer.renderTriangleRgb((float) (local30 - 4), (float) (local30 - 4), (float) local30, (float) (local26 + 4), (float) local26, (float) (local26 + 4), 100.0F, 100.0F, 100.0F, local67.colourNe, local67.colourNw, local67.colourSe);
                            rasterizer.renderTriangleRgb((float) local30, (float) local30, (float) (local30 - 4), (float) local26, (float) (local26 + 4), (float) local26, 100.0F, 100.0F, 100.0F, local67.colourSw, local67.colourSe, local67.colourNw);
                        } else {
                            local89 = local67.blendedColour;
                            rasterizer.renderTriangleRgb((float) (local30 - 4), (float) (local30 - 4), (float) local30, (float) (local26 + 4), (float) local26, (float) (local26 + 4), 100.0F, 100.0F, 100.0F, Static462.blendArgb(local67.colourNe & 0xFF000000, local89), Static462.blendArgb(local67.colourNw & 0xFF000000, local89), Static462.blendArgb(local67.colourSe & 0xFF000000, local89));
                            rasterizer.renderTriangleRgb((float) local30, (float) local30, (float) (local30 - 4), (float) local26, (float) (local26 + 4), (float) local26, 100.0F, 100.0F, 100.0F, Static462.blendArgb(local67.colourSw & 0xFF000000, local89), Static462.blendArgb(local67.colourSe & 0xFF000000, local89), Static462.blendArgb(local67.colourNw & 0xFF000000, local89));
                        }
                    } else if (this.complexBlendedTiles[local32][local35] != null) {
                        @Pc(341) JavaComplexBlendedTile local341 = this.complexBlendedTiles[local32][local35];
                        for (local89 = 0; local89 < local341.vertexCount; local89++) {
                            screenX[local89] = local26 + local341.verticesX[local89] * 4 / super.tileSize;
                            screenY[local89] = local30 - local341.verticesZ[local89] * 4 / super.tileSize;
                        }
                        for (local379 = 0; local379 < local341.faceCount; local379++) {
                            local384 = local379 * 3;
                            local388 = local384 + 1;
                            local392 = local388 + 1;
                            local396 = screenX[local384];
                            local400 = screenX[local388];
                            local404 = screenX[local392];
                            local408 = screenY[local384];
                            local412 = screenY[local388];
                            local416 = screenY[local392];
                            if (local341.faceBlendedColours != null && local341.faceBlendedColours[local379] != 0 && (local341.faceTextures == null || local341.faceTextures != null && local341.faceTextures[local379] == -1)) {
                                local449 = local341.faceBlendedColours[local379];
                                rasterizer.renderTriangleRgb((float) local408, (float) local412, (float) local416, (float) local396, (float) local400, (float) local404, 100.0F, 100.0F, 100.0F, Static462.blendArgb(-(local341.vertexColours[local384] & -16777216) - 16777216, local449), Static462.blendArgb(-(local341.vertexColours[local388] & -16777216) - 16777216, local449), Static462.blendArgb(-(local341.vertexColours[local392] & -16777216) - 16777216, local449));
                            } else if (local341.faceTextures == null || local341.faceTextures[local379] == -1) {
                                rasterizer.renderTriangleRgb((float) local408, (float) local412, (float) local416, (float) local396, (float) local400, (float) local404, 100.0F, 100.0F, 100.0F, local341.vertexColours[local384], local341.vertexColours[local388], local341.vertexColours[local392]);
                            } else {
                                local449 = this.toolkit.textureHsl(local341.faceTextures[local379]);
                                rasterizer.renderTriangleHslRgb((float) local408, (float) local412, (float) local416, (float) local396, (float) local400, (float) local404, 100.0F, 100.0F, 100.0F, (float) local449, (float) local449, (float) local449);
                            }
                        }
                    }
                }
                local30 -= 4;
            }
            local30 = local7;
            local26 += 4;
        }
        rasterizer.fastScanline = true;
        this.toolkit.C(local14);
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(IILclient!lb;[I[I[I[II)V")
    public void renderUnblendedTile(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) Rasterizer rasterizer, @OriginalArg(3) int[] screenX, @OriginalArg(4) int[] screenY, @OriginalArg(5) int[] depths, @OriginalArg(6) int[] fogLevels, @OriginalArg(7) int skipFlags) {
        @Pc(6) JavaSimpleTile local6 = this.simpleTiles[x][z];
        @Pc(50) int local50;
        @Pc(60) int local60;
        @Pc(480) int local480;
        @Pc(510) int local510;
        if (local6 == null) {
            @Pc(2204) JavaComplexTile local2204 = this.complexTiles[x][z];
            if (local2204 != null) {
                if (skipFlags != 0) {
                    if ((local2204.flags & 0x4) == 0) {
                        if ((skipFlags & 0x2) != 0) {
                            return;
                        }
                    } else if ((skipFlags & 0x1) != 0) {
                        return;
                    }
                }
                @Pc(2253) short local2253;
                @Pc(2314) float local2314;
                @Pc(2335) float local2335;
                @Pc(2284) float local2284;
                if (this.depthOverride == -1) {
                    for (local480 = 0; local480 < local2204.vertexCount; local480++) {
                        local50 = local2204.verticesX[local480] + (x << super.tileSizeShift);
                        local2253 = local2204.verticesY[local480];
                        local60 = local2204.verticesZ[local480] + (z << super.tileSizeShift);
                        local2284 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local2253 + this.cameraE3_3 * (float) local60);
                        if (local2284 <= (float) this.toolkit.zNear) {
                            return;
                        }
                        local2314 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local2253 + this.cameraE1_3 * (float) local60);
                        local2335 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local2253 + this.cameraE2_3 * (float) local60);
                        screenX[local480] = rasterizer.minX + (int) (local2314 * (float) this.toolkit.projectionScaleX / local2284);
                        screenY[local480] = rasterizer.minY + (int) (local2335 * (float) this.toolkit.projectionScaleY / local2284);
                        depths[local480] = (int) local2284;
                    }
                } else {
                    for (local480 = 0; local480 < local2204.vertexCount; local480++) {
                        local50 = local2204.verticesX[local480] + (x << super.tileSizeShift);
                        local2253 = local2204.verticesY[local480];
                        local60 = local2204.verticesZ[local480] + (z << super.tileSizeShift);
                        local2284 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local2253 + this.cameraE3_3 * (float) local60);
                        local2314 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local2253 + this.cameraE1_3 * (float) local60);
                        local2335 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local2253 + this.cameraE2_3 * (float) local60);
                        screenX[local480] = rasterizer.minX + (int) (local2314 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        screenY[local480] = rasterizer.minY + (int) (local2335 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                        depths[local480] = (int) local2284;
                    }
                }
                @Pc(2531) short local2531;
                @Pc(2536) short local2536;
                @Pc(2541) short local2541;
                @Pc(2549) int local2549;
                @Pc(2553) int local2553;
                @Pc(2557) int local2557;
                @Pc(2561) int local2561;
                @Pc(2565) int local2565;
                if (local2204.faceTextures != null) {
                    @Pc(2753) int local2753;
                    @Pc(2622) short local2622;
                    if (this.depthOverride == -1) {
                        for (local480 = 0; local480 < local2204.faceCount; local480++) {
                            local2531 = local2204.faceA[local480];
                            local2536 = local2204.faceB[local480];
                            local2541 = local2204.faceC[local480];
                            local510 = screenX[local2531];
                            local2549 = screenX[local2536];
                            local2553 = screenX[local2541];
                            local2557 = screenY[local2531];
                            local2561 = screenY[local2536];
                            local2565 = screenY[local2541];
                            if ((local510 - local2549) * (local2565 - local2561) - (local2557 - local2561) * (local2553 - local2549) > 0) {
                                rasterizer.clamp = local510 < 0 || local2549 < 0 || local2553 < 0 || local510 > rasterizer.width || local2549 > rasterizer.width || local2553 > rasterizer.width;
                                local2622 = local2204.faceTextures[local480];
                                if (local2622 == -1) {
                                    local2753 = local2204.faceColours[local480];
                                    if (local2753 != -1) {
                                        rasterizer.renderTriangleHslRgb((float) local2557, (float) local2561, (float) local2565, (float) local510, (float) local2549, (float) local2553, (float) depths[local2531], (float) depths[local2536], (float) depths[local2541], (float) Static244.scaleHslLightness(local2204.verticesLight[local2531], local2753), (float) Static244.scaleHslLightness(local2204.verticesLight[local2536], local2753), (float) Static244.scaleHslLightness(local2204.verticesLight[local2541], local2753));
                                    }
                                } else {
                                    rasterizer.renderTexturedTriangleRgb((float) local2557, (float) local2561, (float) local2565, (float) local510, (float) local2549, (float) local2553, (float) depths[local2531], (float) depths[local2536], (float) depths[local2541], (float) local2204.verticesX[local2531] / (float) super.tileSize, (float) local2204.verticesX[local2536] / (float) super.tileSize, (float) local2204.verticesX[local2541] / (float) super.tileSize, (float) local2204.verticesZ[local2531] / (float) super.tileSize, (float) local2204.verticesZ[local2536] / (float) super.tileSize, (float) local2204.verticesZ[local2541] / (float) super.tileSize, ColourUtils.HSV_TO_RGB[local2204.verticesLight[local2531] & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local2204.verticesLight[local2536] & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local2204.verticesLight[local2541] & 0xFFFF] & 0xFFFFFF | 0xFF000000, 0, 0, 0, 0, local2622);
                                }
                            }
                        }
                        return;
                    }
                    for (local480 = 0; local480 < local2204.faceCount; local480++) {
                        local2531 = local2204.faceA[local480];
                        local2536 = local2204.faceB[local480];
                        local2541 = local2204.faceC[local480];
                        local510 = screenX[local2531];
                        local2549 = screenX[local2536];
                        local2553 = screenX[local2541];
                        local2557 = screenY[local2531];
                        local2561 = screenY[local2536];
                        local2565 = screenY[local2541];
                        if ((local510 - local2549) * (local2565 - local2561) - (local2557 - local2561) * (local2553 - local2549) > 0) {
                            rasterizer.clamp = local510 < 0 || local2549 < 0 || local2553 < 0 || local510 > rasterizer.width || local2549 > rasterizer.width || local2553 > rasterizer.width;
                            local2622 = local2204.faceTextures[local480];
                            if (local2622 == -1) {
                                local2753 = local2204.faceColours[local480];
                                if (local2753 != -1) {
                                    rasterizer.renderTriangleHslRgb((float) local2557, (float) local2561, (float) local2565, (float) local510, (float) local2549, (float) local2553, (float) depths[local2531], (float) depths[local2536], (float) depths[local2541], (float) Static244.scaleHslLightness(local2204.verticesLight[local2531], local2753), (float) Static244.scaleHslLightness(local2204.verticesLight[local2536], local2753), (float) Static244.scaleHslLightness(local2204.verticesLight[local2541], local2753));
                                }
                            } else {
                                rasterizer.renderTexturedTriangleRgb((float) local2557, (float) local2561, (float) local2565, (float) local510, (float) local2549, (float) local2553, (float) depths[local2531], (float) depths[local2536], (float) depths[local2541], (float) local2204.verticesX[local2531] / (float) super.tileSize, (float) local2204.verticesX[local2536] / (float) super.tileSize, (float) local2204.verticesX[local2541] / (float) super.tileSize, (float) local2204.verticesZ[local2531] / (float) super.tileSize, (float) local2204.verticesZ[local2536] / (float) super.tileSize, (float) local2204.verticesZ[local2541] / (float) super.tileSize, ColourUtils.HSV_TO_RGB[local2204.verticesLight[local2531] & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local2204.verticesLight[local2536] & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local2204.verticesLight[local2541] & 0xFFFF] & 0xFFFFFF | 0xFF000000, 0, 0, 0, 0, local2622);
                            }
                        }
                    }
                    return;
                }
                for (local480 = 0; local480 < local2204.faceCount; local480++) {
                    local2531 = local2204.faceA[local480];
                    local2536 = local2204.faceB[local480];
                    local2541 = local2204.faceC[local480];
                    local510 = screenX[local2531];
                    local2549 = screenX[local2536];
                    local2553 = screenX[local2541];
                    local2557 = screenY[local2531];
                    local2561 = screenY[local2536];
                    local2565 = screenY[local2541];
                    if ((local510 - local2549) * (local2565 - local2561) - (local2557 - local2561) * (local2553 - local2549) > 0) {
                        @Pc(3170) int local3170 = local2204.faceColours[local480];
                        if (local3170 != -1) {
                            rasterizer.clamp = local510 < 0 || local2549 < 0 || local2553 < 0 || local510 > rasterizer.width || local2549 > rasterizer.width || local2553 > rasterizer.width;
                            rasterizer.renderTriangleHslRgb((float) local2557, (float) local2561, (float) local2565, (float) local510, (float) local2549, (float) local2553, (float) depths[local2531], (float) depths[local2536], (float) depths[local2541], (float) Static244.scaleHslLightness(local2204.verticesLight[local2531], local3170), (float) Static244.scaleHslLightness(local2204.verticesLight[local2536], local3170), (float) Static244.scaleHslLightness(local2204.verticesLight[local2541], local3170));
                        }
                    }
                }
            }
        } else if ((local6.flags & 0x2) == 0) {
            if (skipFlags != 0) {
                if ((local6.flags & 0x4) == 0) {
                    if ((skipFlags & 0x2) != 0) {
                        return;
                    }
                } else if ((skipFlags & 0x1) != 0) {
                    return;
                }
            }
            @Pc(45) int local45 = x * super.tileSize;
            local50 = local45 + super.tileSize;
            @Pc(55) int local55 = z * super.tileSize;
            local60 = local55 + super.tileSize;
            @Pc(497) float local497;
            @Pc(99) float local99;
            @Pc(125) float local125;
            @Pc(151) float local151;
            @Pc(177) float local177;
            @Pc(72) int local72;
            @Pc(300) int local300;
            @Pc(360) int local360;
            @Pc(420) int local420;
            @Pc(330) int local330;
            @Pc(390) int local390;
            @Pc(450) int local450;
            @Pc(287) float local287;
            @Pc(347) float local347;
            @Pc(407) float local407;
            @Pc(467) float local467;
            @Pc(317) float local317;
            @Pc(377) float local377;
            @Pc(437) float local437;
            if ((local6.flags & 0x1) == 0) {
                local72 = super.tileHeights[x][z];
                @Pc(784) int local784 = super.tileHeights[x + 1][z];
                @Pc(795) int local795 = super.tileHeights[x + 1][z + 1];
                @Pc(804) int local804 = super.tileHeights[x][z + 1];
                if (this.depthOverride == -1) {
                    local99 = this.cameraTz + (this.cameraE3_1 * (float) local45 + this.cameraE3_2 * (float) local72 + this.cameraE3_3 * (float) local55);
                    if (local99 <= (float) this.toolkit.zNear) {
                        return;
                    }
                    local125 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local784 + this.cameraE3_3 * (float) local55);
                    if (local125 <= (float) this.toolkit.zNear) {
                        return;
                    }
                    local151 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local795 + this.cameraE3_3 * (float) local60);
                    if (local151 <= (float) this.toolkit.zNear) {
                        return;
                    }
                    local177 = this.cameraTz + (this.cameraE3_1 * (float) local45 + this.cameraE3_2 * (float) local804 + this.cameraE3_3 * (float) local60);
                    if (local177 <= (float) this.toolkit.zNear) {
                        return;
                    }
                    local287 = this.cameraTx + (this.cameraE1_1 * (float) local45 + this.cameraE1_2 * (float) local72 + this.cameraE1_3 * (float) local55);
                    local300 = rasterizer.minX + (int) (local287 * (float) this.toolkit.projectionScaleX / local99);
                    local317 = this.cameraTy + (this.cameraE2_1 * (float) local45 + this.cameraE2_2 * (float) local72 + this.cameraE2_3 * (float) local55);
                    local330 = rasterizer.minY + (int) (local317 * (float) this.toolkit.projectionScaleY / local99);
                    local347 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local784 + this.cameraE1_3 * (float) local55);
                    local360 = rasterizer.minX + (int) (local347 * (float) this.toolkit.projectionScaleX / local125);
                    local377 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local784 + this.cameraE2_3 * (float) local55);
                    local390 = rasterizer.minY + (int) (local377 * (float) this.toolkit.projectionScaleY / local125);
                    local407 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local795 + this.cameraE1_3 * (float) local60);
                    local420 = rasterizer.minX + (int) (local407 * (float) this.toolkit.projectionScaleX / local151);
                    local437 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local795 + this.cameraE2_3 * (float) local60);
                    local450 = rasterizer.minY + (int) (local437 * (float) this.toolkit.projectionScaleY / local151);
                    local467 = this.cameraTx + (this.cameraE1_1 * (float) local45 + this.cameraE1_2 * (float) local804 + this.cameraE1_3 * (float) local60);
                    local480 = rasterizer.minX + (int) (local467 * (float) this.toolkit.projectionScaleX / local177);
                    local497 = this.cameraTy + (this.cameraE2_1 * (float) local45 + this.cameraE2_2 * (float) local804 + this.cameraE2_3 * (float) local60);
                    local510 = rasterizer.minY + (int) (local497 * (float) this.toolkit.projectionScaleY / local177);
                } else {
                    local99 = this.cameraTz + (this.cameraE3_1 * (float) local45 + this.cameraE3_2 * (float) local72 + this.cameraE3_3 * (float) local55);
                    local125 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local784 + this.cameraE3_3 * (float) local55);
                    local151 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local795 + this.cameraE3_3 * (float) local60);
                    local177 = this.cameraTz + (this.cameraE3_1 * (float) local45 + this.cameraE3_2 * (float) local804 + this.cameraE3_3 * (float) local60);
                    local287 = this.cameraTx + (this.cameraE1_1 * (float) local45 + this.cameraE1_2 * (float) local72 + this.cameraE1_3 * (float) local55);
                    local300 = rasterizer.minX + (int) (local287 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                    local317 = this.cameraTy + (this.cameraE2_1 * (float) local45 + this.cameraE2_2 * (float) local72 + this.cameraE2_3 * (float) local55);
                    local330 = rasterizer.minY + (int) (local317 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                    local347 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local784 + this.cameraE1_3 * (float) local55);
                    local360 = rasterizer.minX + (int) (local347 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                    local377 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local784 + this.cameraE2_3 * (float) local55);
                    local390 = rasterizer.minY + (int) (local377 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                    local407 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local795 + this.cameraE1_3 * (float) local60);
                    local420 = rasterizer.minX + (int) (local407 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                    local437 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local795 + this.cameraE2_3 * (float) local60);
                    local450 = rasterizer.minY + (int) (local437 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                    local467 = this.cameraTx + (this.cameraE1_1 * (float) local45 + this.cameraE1_2 * (float) local804 + this.cameraE1_3 * (float) local60);
                    local480 = rasterizer.minX + (int) (local467 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                    local497 = this.cameraTy + (this.cameraE2_1 * (float) local45 + this.cameraE2_2 * (float) local804 + this.cameraE2_3 * (float) local60);
                    local510 = rasterizer.minY + (int) (local497 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                }
            } else {
                local72 = super.tileHeights[x][z];
                @Pc(78) float local78 = this.cameraE3_2 * (float) local72;
                if (this.depthOverride == -1) {
                    local99 = this.cameraTz + (this.cameraE3_1 * (float) local45 + local78 + this.cameraE3_3 * (float) local55);
                    if (local99 <= (float) this.toolkit.zNear) {
                        return;
                    }
                    local125 = this.cameraTz + (this.cameraE3_1 * (float) local50 + local78 + this.cameraE3_3 * (float) local55);
                    if (local125 <= (float) this.toolkit.zNear) {
                        return;
                    }
                    local151 = this.cameraTz + (this.cameraE3_1 * (float) local50 + local78 + this.cameraE3_3 * (float) local60);
                    if (local151 <= (float) this.toolkit.zNear) {
                        return;
                    }
                    local177 = this.cameraTz + (this.cameraE3_1 * (float) local45 + local78 + this.cameraE3_3 * (float) local60);
                    if (local177 <= (float) this.toolkit.zNear) {
                        return;
                    }
                } else {
                    local99 = this.cameraTz + (this.cameraE3_1 * (float) local45 + local78 + this.cameraE3_3 * (float) local55);
                    local125 = this.cameraTz + (this.cameraE3_1 * (float) local50 + local78 + this.cameraE3_3 * (float) local55);
                    local151 = this.cameraTz + (this.cameraE3_1 * (float) local50 + local78 + this.cameraE3_3 * (float) local60);
                    local177 = this.cameraTz + (this.cameraE3_1 * (float) local45 + local78 + this.cameraE3_3 * (float) local60);
                }
                @Pc(260) float local260 = this.cameraE1_2 * (float) local72;
                @Pc(266) float local266 = this.cameraE2_2 * (float) local72;
                if (this.depthOverride == -1) {
                    local287 = this.cameraTx + (this.cameraE1_1 * (float) local45 + local260 + this.cameraE1_3 * (float) local55);
                    local300 = rasterizer.minX + (int) (local287 * (float) this.toolkit.projectionScaleX / local99);
                    local317 = this.cameraTy + (this.cameraE2_1 * (float) local45 + local266 + this.cameraE2_3 * (float) local55);
                    local330 = rasterizer.minY + (int) (local317 * (float) this.toolkit.projectionScaleY / local99);
                    local347 = this.cameraTx + (this.cameraE1_1 * (float) local50 + local260 + this.cameraE1_3 * (float) local55);
                    local360 = rasterizer.minX + (int) (local347 * (float) this.toolkit.projectionScaleX / local125);
                    local377 = this.cameraTy + (this.cameraE2_1 * (float) local50 + local266 + this.cameraE2_3 * (float) local55);
                    local390 = rasterizer.minY + (int) (local377 * (float) this.toolkit.projectionScaleY / local125);
                    local407 = this.cameraTx + (this.cameraE1_1 * (float) local50 + local260 + this.cameraE1_3 * (float) local60);
                    local420 = rasterizer.minX + (int) (local407 * (float) this.toolkit.projectionScaleX / local151);
                    local437 = this.cameraTy + (this.cameraE2_1 * (float) local50 + local266 + this.cameraE2_3 * (float) local60);
                    local450 = rasterizer.minY + (int) (local437 * (float) this.toolkit.projectionScaleY / local151);
                    local467 = this.cameraTx + (this.cameraE1_1 * (float) local45 + local260 + this.cameraE1_3 * (float) local60);
                    local480 = rasterizer.minX + (int) (local467 * (float) this.toolkit.projectionScaleX / local177);
                    local497 = this.cameraTy + (this.cameraE2_1 * (float) local45 + local266 + this.cameraE2_3 * (float) local60);
                    local510 = rasterizer.minY + (int) (local497 * (float) this.toolkit.projectionScaleY / local177);
                } else {
                    local287 = this.cameraTx + (this.cameraE1_1 * (float) local45 + local260 + this.cameraE1_3 * (float) local55);
                    local300 = rasterizer.minX + (int) (local287 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                    local317 = this.cameraTy + (this.cameraE2_1 * (float) local45 + local266 + this.cameraE2_3 * (float) local55);
                    local330 = rasterizer.minY + (int) (local317 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                    local347 = this.cameraTx + (this.cameraE1_1 * (float) local50 + local260 + this.cameraE1_3 * (float) local55);
                    local360 = rasterizer.minX + (int) (local347 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                    local377 = this.cameraTy + (this.cameraE2_1 * (float) local50 + local266 + this.cameraE2_3 * (float) local55);
                    local390 = rasterizer.minY + (int) (local377 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                    local407 = this.cameraTx + (this.cameraE1_1 * (float) local50 + local260 + this.cameraE1_3 * (float) local60);
                    local420 = rasterizer.minX + (int) (local407 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                    local437 = this.cameraTy + (this.cameraE2_1 * (float) local50 + local266 + this.cameraE2_3 * (float) local60);
                    local450 = rasterizer.minY + (int) (local437 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                    local467 = this.cameraTx + (this.cameraE1_1 * (float) local45 + local260 + this.cameraE1_3 * (float) local60);
                    local480 = rasterizer.minX + (int) (local467 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                    local497 = this.cameraTy + (this.cameraE2_1 * (float) local45 + local266 + this.cameraE2_3 * (float) local60);
                    local510 = rasterizer.minY + (int) (local497 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                }
            }
            if (this.depthOverride == -1) {
                if ((local420 - local480) * (local390 - local510) - (local450 - local510) * (local360 - local480) > 0) {
                    rasterizer.clamp = local420 < 0 || local480 < 0 || local360 < 0 || local420 > rasterizer.width || local480 > rasterizer.width || local360 > rasterizer.width;
                    if (local6.texture >= 0) {
                        rasterizer.renderTexturedTriangleRgb((float) local450, (float) local510, (float) local390, (float) local420, (float) local480, (float) local360, local151, local177, local125, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, ColourUtils.HSV_TO_RGB[local6.hslNe & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local6.hslNw & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local6.hslSe & 0xFFFF] & 0xFFFFFF | 0xFF000000, 0, 0, 0, 0, local6.texture);
                    } else {
                        rasterizer.renderTriangleHslRgb((float) local450, (float) local510, (float) local390, (float) local420, (float) local480, (float) local360, (float) (int) local151, (float) (int) local177, (float) (int) local125, (float) (local6.hslNe & 0xFFFF), (float) (local6.hslNw & 0xFFFF), (float) (local6.hslSe & 0xFFFF));
                    }
                }
                if ((local300 - local360) * (local510 - local390) - (local330 - local390) * (local480 - local360) > 0) {
                    rasterizer.clamp = local300 < 0 || local360 < 0 || local480 < 0 || local300 > rasterizer.width || local360 > rasterizer.width || local480 > rasterizer.width;
                    if (local6.texture >= 0) {
                        rasterizer.renderTexturedTriangleRgb((float) local330, (float) local390, (float) local510, (float) local300, (float) local360, (float) local480, local99, local125, local177, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, ColourUtils.HSV_TO_RGB[local6.hslSw & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local6.hslSe & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local6.hslNw & 0xFFFF] & 0xFFFFFF | 0xFF000000, 0, 0, 0, 0, local6.texture);
                        return;
                    }
                    rasterizer.renderTriangleHslRgb((float) local330, (float) local390, (float) local510, (float) local300, (float) local360, (float) local480, (float) (int) local99, (float) (int) local125, (float) (int) local177, (float) (local6.hslSw & 0xFFFF), (float) (local6.hslSe & 0xFFFF), (float) (local6.hslNw & 0xFFFF));
                    return;
                }
            } else {
                if ((local420 - local480) * (local390 - local510) - (local450 - local510) * (local360 - local480) > 0) {
                    rasterizer.clamp = local420 < 0 || local480 < 0 || local360 < 0 || local420 > rasterizer.width || local480 > rasterizer.width || local360 > rasterizer.width;
                    if (local6.texture >= 0) {
                        rasterizer.renderTexturedTriangleRgb((float) local450, (float) local510, (float) local390, (float) local420, (float) local480, (float) local360, local151, local177, local125, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, ColourUtils.HSV_TO_RGB[local6.hslNe & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local6.hslNw & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local6.hslSe & 0xFFFF] & 0xFFFFFF | 0xFF000000, 0, 0, 0, 0, local6.texture);
                    } else {
                        rasterizer.renderTriangleHslRgb((float) local450, (float) local510, (float) local390, (float) local420, (float) local480, (float) local360, (float) (int) local151, (float) (int) local177, (float) (int) local125, (float) (local6.hslNe & 0xFFFF), (float) (local6.hslNw & 0xFFFF), (float) (local6.hslSe & 0xFFFF));
                    }
                }
                if ((local300 - local360) * (local510 - local390) - (local330 - local390) * (local480 - local360) > 0) {
                    rasterizer.clamp = local300 < 0 || local360 < 0 || local480 < 0 || local300 > rasterizer.width || local360 > rasterizer.width || local480 > rasterizer.width;
                    if (local6.texture >= 0) {
                        rasterizer.renderTexturedTriangleRgb((float) local330, (float) local390, (float) local510, (float) local300, (float) local360, (float) local480, local99, local125, local177, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, ColourUtils.HSV_TO_RGB[local6.hslSw & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local6.hslSe & 0xFFFF] & 0xFFFFFF | 0xFF000000, ColourUtils.HSV_TO_RGB[local6.hslNw & 0xFFFF] & 0xFFFFFF | 0xFF000000, 0, 0, 0, 0, local6.texture);
                        return;
                    }
                    rasterizer.renderTriangleHslRgb((float) local330, (float) local390, (float) local510, (float) local300, (float) local360, (float) local480, (float) (int) local99, (float) (int) local125, (float) (int) local177, (float) (local6.hslSw & 0xFFFF), (float) (local6.hslSe & 0xFFFF), (float) (local6.hslNw & 0xFFFF));
                }
            }
        }
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(III[[ZZII)V")
    @Override
    public void renderTilesAtDepth(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) int radius, @OriginalArg(3) boolean[][] visibility, @OriginalArg(4) boolean arg4, @OriginalArg(5) int depth, @OriginalArg(6) int skipFlags) {
        @Pc(3) JavaMatrix local3 = this.toolkit.camera;
        this.depthOverride = depth;
        this.cameraE1_1 = local3.e1_1;
        this.cameraE1_2 = local3.e1_2;
        this.cameraE1_3 = local3.e1_3;
        this.cameraTx = local3.tx;
        this.cameraE2_1 = local3.e2_1;
        this.cameraE2_2 = local3.e2_2;
        this.cameraE2_3 = local3.e2_3;
        this.cameraTy = local3.ty;
        this.cameraE3_1 = local3.e3_1;
        this.cameraE3_2 = local3.e3_2;
        this.cameraE3_3 = local3.e3_3;
        this.cameraTz = local3.tz;
        for (@Pc(56) int local56 = 0; local56 < radius + radius; local56++) {
            for (@Pc(59) int local59 = 0; local59 < radius + radius; local59++) {
                if (visibility[local56][local59]) {
                    @Pc(72) int local72 = x + local56 - radius;
                    @Pc(78) int local78 = z + local59 - radius;
                    if (local72 >= 0 && local72 < super.sizeX && local78 >= 0 && local78 < super.sizeZ) {
                        this.renderTile(local72, local78, skipFlags);
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(IIIIIII[[Z)V")
    @Override
    public void drawMinimap(@OriginalArg(3) int x1, @OriginalArg(4) int y1, @OriginalArg(5) int x2, @OriginalArg(6) int y2, @OriginalArg(7) boolean[][] visibility) {
        @Pc(4) JavaThreadResource local4 = this.toolkit.threadResource(Thread.currentThread());
        @Pc(7) Rasterizer local7 = local4.rasterizer;
        local7.alpha = 0;
        local7.clamp = true;
        this.toolkit.ya();
        if (this.simpleBlendedTiles != null || this.genericBlendedTiles != null) {
            this.drawBlendedMinimapTiles(x1, y1, x2, y2, visibility, local4, local7, local4.screenX, local4.screenY);
        } else if (this.simpleTiles != null) {
            this.drawUnblendedMinimapTiles(x1, y1, x2, y2, visibility, local4, local7, local4.screenX, local4.screenY);
        }
    }

    @OriginalMember(owner = "client!qs", name = "wa", descriptor = "(Lclient!r;IIIIZ)V")
    @Override
    public void wa(@OriginalArg(0) Shadow arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) boolean arg5) {
    }

    @OriginalMember(owner = "client!qs", name = "a", descriptor = "(II[I[I[I[I[I[I[I[I[I[I[IIIIZ)V")
    @Override
    public void addTile(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) int[] offsetX, @OriginalArg(3) int[] offsetLevel, @OriginalArg(4) int[] offsetY, @OriginalArg(5) int[] depths, @OriginalArg(6) int[] faceA, @OriginalArg(7) int[] faceB, @OriginalArg(8) int[] faceC, @OriginalArg(9) int[] colours, @OriginalArg(10) int[] blendedColours, @OriginalArg(11) int[] textures, @OriginalArg(12) int[] sizes, @OriginalArg(13) int waterColour, @OriginalArg(14) int waterDepth, @OriginalArg(15) int waterBias) {
        if (this.simpleTiles == null) {
            this.simpleTiles = new JavaSimpleTile[super.sizeX][super.sizeZ];
            this.complexTiles = new JavaComplexTile[super.sizeX][super.sizeZ];
        } else if (this.simpleBlendedTiles != null || this.genericBlendedTiles != null) {
            throw new IllegalStateException();
        }
        @Pc(33) boolean local33 = false;
        @Pc(79) int local79;
        @Pc(85) int local85;
        if (colours.length == 2 && faceA.length == 2 && (colours[0] == colours[1] || textures[0] != -1 && textures[0] == textures[1])) {
            local33 = true;
            for (@Pc(72) int local72 = 1; local72 < 2; local72++) {
                local79 = offsetX[faceA[local72]];
                local85 = offsetY[faceA[local72]];
                if (local79 != 0 && local79 != super.tileSize || local85 != 0 && local85 != super.tileSize) {
                    local33 = false;
                    break;
                }
            }
        }
        if (!local33) {
            @Pc(118) JavaComplexTile local118 = new JavaComplexTile();
            @Pc(122) short local122 = (short) offsetX.length;
            @Pc(126) short local126 = (short) colours.length;
            local118.vertexCount = local122;
            local118.verticesLight = new short[local122];
            local118.verticesX = new short[local122];
            local118.verticesY = new short[local122];
            local118.verticesZ = new short[local122];
            @Pc(156) int local156;
            @Pc(323) int local323;
            @Pc(370) int local370;
            for (@Pc(147) int local147 = 0; local147 < local122; local147++) {
                @Pc(152) int local152 = offsetX[local147];
                local156 = offsetY[local147];
                if (local152 == 0 && local156 == 0) {
                    local118.verticesLight[local147] = (short) (this.lightLevels[x][z] - this.shadowLevels[x][z]);
                } else if (local152 == 0 && local156 == super.tileSize) {
                    local118.verticesLight[local147] = (short) (this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1]);
                } else if (local152 == super.tileSize && local156 == super.tileSize) {
                    local118.verticesLight[local147] = (short) (this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1]);
                } else if (local152 == super.tileSize && local156 == 0) {
                    local118.verticesLight[local147] = (short) (this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z]);
                } else {
                    local323 = (this.lightLevels[x][z] - this.shadowLevels[x][z]) * (super.tileSize - local152) + (this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z]) * local152;
                    local370 = (this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1]) * (super.tileSize - local152) + (this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1]) * local152;
                    local118.verticesLight[local147] = (short) (local323 * (super.tileSize - local156) + local370 * local156 >> super.tileSizeShift * 2);
                }
                local323 = (x << super.tileSizeShift) + local152;
                local370 = (z << super.tileSizeShift) + local156;
                local118.verticesX[local147] = (short) local152;
                local118.verticesZ[local147] = (short) local156;
                local118.verticesY[local147] = (short) (this.averageHeight(local323, local370) + (offsetLevel == null ? 0 : offsetLevel[local147]));
                if (local118.verticesLight[local147] < 2) {
                    local118.verticesLight[local147] = 2;
                }
            }
            @Pc(454) boolean local454 = false;
            local156 = 0;
            for (local323 = 0; local323 < local126; local323++) {
                if (colours[local323] >= 0 || blendedColours != null && blendedColours[local323] >= 0) {
                    local156++;
                }
                local370 = textures[local323];
                if (local370 != -1) {
                    @Pc(490) TextureMetrics local490 = this.toolkit.textureSource.getMetrics(local370);
                    if (!local490.disableable) {
                        local454 = true;
                        if (this.isWaterEffect(local490.effectType) || local490.speedU != 0 || local490.speedV != 0) {
                            local118.flags = (byte) (local118.flags | 0x4);
                        }
                    }
                }
            }
            local118.faceColours = new int[local156];
            if (blendedColours != null) {
                local118.faceBlendedColours = new int[local156];
            }
            local118.faceA = new short[local156];
            local118.faceB = new short[local156];
            local118.faceC = new short[local156];
            if (local454) {
                local118.faceTextures = new short[local156];
                local118.faceSizes = new short[local156];
            }
            for (local370 = 0; local370 < local126; local370++) {
                if (colours[local370] >= 0 || blendedColours != null && blendedColours[local370] >= 0) {
                    if (colours[local370] >= 0) {
                        local118.faceColours[local118.faceCount] = ColourUtils.hslToHsv(colours[local370]);
                    } else {
                        local118.faceColours[local118.faceCount] = -1;
                    }
                    if (blendedColours != null) {
                        if (blendedColours[local370] == -1) {
                            local118.faceBlendedColours[local118.faceCount] = -1;
                        } else {
                            local118.faceBlendedColours[local118.faceCount] = ColourUtils.hslToHsv(blendedColours[local370]);
                        }
                    }
                    local118.faceA[local118.faceCount] = (short) faceA[local370];
                    local118.faceB[local118.faceCount] = (short) faceB[local370];
                    local118.faceC[local118.faceCount] = (short) faceC[local370];
                    if (local454) {
                        if (textures[local370] == -1 || this.toolkit.textureSource.getMetrics(textures[local370]).disableable) {
                            local118.faceTextures[local118.faceCount] = -1;
                        } else {
                            local118.faceTextures[local118.faceCount] = (short) textures[local370];
                            local118.faceSizes[local118.faceCount] = (short) sizes[local370];
                        }
                    }
                    local118.faceCount++;
                }
            }
            this.complexTiles[x][z] = local118;
        } else if (colours[0] >= 0 || blendedColours != null && blendedColours[0] >= 0) {
            @Pc(741) JavaSimpleTile local741 = new JavaSimpleTile();
            local79 = colours[0];
            local85 = textures[0];
            if (blendedColours != null) {
                local741.blendedColour = Static244.scaleHslLightness(this.lightLevels[x][z] - this.shadowLevels[x][z], ColourUtils.hslToHsv(blendedColours[0]));
                if (local79 == -1) {
                    local741.flags = (byte) (local741.flags | 0x2);
                }
            }
            if (super.tileHeights[x][z] == super.tileHeights[x + 1][z] && super.tileHeights[x][z] == super.tileHeights[x + 1][z + 1] && super.tileHeights[x][z] == super.tileHeights[x][z + 1]) {
                local741.flags = (byte) (local741.flags | 0x1);
            }
            @Pc(849) TextureMetrics local849 = null;
            if (local85 != -1) {
                local849 = this.toolkit.textureSource.getMetrics(local85);
            }
            if (local849 == null || (local741.flags & 0x2) != 0 || local849.disableable) {
                @Pc(987) short local987 = ColourUtils.hslToHsv(local79);
                local741.hslSw = (short) Static244.scaleHslLightness(this.lightLevels[x][z] - this.shadowLevels[x][z], local987);
                local741.hslSe = (short) Static244.scaleHslLightness(this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z], local987);
                local741.hslNe = (short) Static244.scaleHslLightness(this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1], local987);
                local741.hslNw = (short) Static244.scaleHslLightness(this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1], local987);
                local741.texture = -1;
            } else {
                local741.hslSw = (short) (this.lightLevels[x][z] - this.shadowLevels[x][z]);
                local741.hslSe = (short) (this.lightLevels[x + 1][z] - this.shadowLevels[x + 1][z]);
                local741.hslNe = (short) (this.lightLevels[x + 1][z + 1] - this.shadowLevels[x + 1][z + 1]);
                local741.hslNw = (short) (this.lightLevels[x][z + 1] - this.shadowLevels[x][z + 1]);
                local741.texture = (short) local85;
                if (this.isWaterEffect(local849.effectType) || local849.speedU != 0 || local849.speedV != 0) {
                    local741.flags = (byte) (local741.flags | 0x4);
                }
            }
            this.simpleTiles[x][z] = local741;
        }
    }

    @OriginalMember(owner = "client!qs", name = "ka", descriptor = "(III)V")
    @Override
    public void ka(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) int shadow) {
        if (this.shadowLevels[x][z] < shadow) {
            this.shadowLevels[x][z] = (byte) shadow;
        }
    }

    @OriginalMember(owner = "client!qs", name = "b", descriptor = "(IIZLclient!wf;Lclient!lb;[I[I[I[II)V")
    public void renderBlendedTile(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) boolean water, @OriginalArg(3) JavaThreadResource resource, @OriginalArg(4) Rasterizer rasterizer, @OriginalArg(5) int[] screenX, @OriginalArg(6) int[] screenY, @OriginalArg(7) int[] depths, @OriginalArg(8) int[] fogLevels, @OriginalArg(9) int skipFlags) {
        @Pc(6) JavaSimpleBlendedTile simpleTile = this.simpleBlendedTiles[x][z];
        @Pc(50) int local50;
        @Pc(55) int local55;
        @Pc(60) int local60;
        @Pc(559) int local559;
        @Pc(409) int local409;
        @Pc(469) int local469;
        @Pc(529) int local529;
        @Pc(589) int local589;

        if (simpleTile != null) {
            if ((simpleTile.flags & 0x2) == 0) {
                if (skipFlags != 0) {
                    if ((simpleTile.flags & 0x4) == 0) {
                        if ((skipFlags & 0x2) != 0) {
                            return;
                        }
                    } else if ((skipFlags & 0x1) != 0) {
                        return;
                    }
                }

                @Pc(45) int local45 = x * super.tileSize;
                local50 = local45 + super.tileSize;
                local55 = z * super.tileSize;
                local60 = local55 + super.tileSize;
                @Pc(62) int local62 = 0;
                @Pc(64) int local64 = 0;
                @Pc(66) int local66 = 0;
                @Pc(68) int local68 = 0;
                @Pc(111) float local111;
                @Pc(137) float local137;
                @Pc(163) float local163;
                @Pc(189) float local189;
                @Pc(276) int local276;
                @Pc(84) int local84;
                @Pc(863) int local863;
                @Pc(874) int local874;
                @Pc(379) int local379;
                @Pc(439) int local439;
                @Pc(499) int local499;
                @Pc(366) float local366;
                @Pc(426) float local426;
                @Pc(486) float local486;
                @Pc(546) float local546;
                @Pc(396) float local396;
                @Pc(456) float local456;
                @Pc(516) float local516;
                @Pc(576) float local576;

                if ((simpleTile.flags & 0x1) != 0 && !water) {
                    local84 = super.tileHeights[x][z];
                    @Pc(90) float local90 = this.cameraE3_2 * (float) local84;

                    if (this.depthOverride == -1) {
                        local111 = this.cameraTz + (this.cameraE3_1 * (float) local45 + local90 + this.cameraE3_3 * (float) local55);
                        if (local111 <= (float) this.toolkit.zNear) {
                            return;
                        }
                        local137 = this.cameraTz + (this.cameraE3_1 * (float) local50 + local90 + this.cameraE3_3 * (float) local55);
                        if (local137 <= (float) this.toolkit.zNear) {
                            return;
                        }
                        local163 = this.cameraTz + (this.cameraE3_1 * (float) local50 + local90 + this.cameraE3_3 * (float) local60);
                        if (local163 <= (float) this.toolkit.zNear) {
                            return;
                        }
                        local189 = this.cameraTz + (this.cameraE3_1 * (float) local45 + local90 + this.cameraE3_3 * (float) local60);
                        if (local189 <= (float) this.toolkit.zNear) {
                            return;
                        }
                    } else {
                        local111 = this.cameraTz + (this.cameraE3_1 * (float) local45 + local90 + this.cameraE3_3 * (float) local55);
                        local137 = this.cameraTz + (this.cameraE3_1 * (float) local50 + local90 + this.cameraE3_3 * (float) local55);
                        local163 = this.cameraTz + (this.cameraE3_1 * (float) local50 + local90 + this.cameraE3_3 * (float) local60);
                        local189 = this.cameraTz + (this.cameraE3_1 * (float) local45 + local90 + this.cameraE3_3 * (float) local60);
                    }

                    if (resource.fogActive) {
                        local276 = (int) (local111 - (float) resource.fogPlane);
                        if (local276 > 0) {
                            local62 = local276;
                            if (local276 > 255) {
                                local62 = 255;
                            }
                        }
                        local276 = (int) (local137 - (float) resource.fogPlane);
                        if (local276 > 0) {
                            local64 = local276;
                            if (local276 > 255) {
                                local64 = 255;
                            }
                        }
                        local276 = (int) (local163 - (float) resource.fogPlane);
                        if (local276 > 0) {
                            local66 = local276;
                            if (local276 > 255) {
                                local66 = 255;
                            }
                        }
                        local276 = (int) (local189 - (float) resource.fogPlane);
                        if (local276 > 0) {
                            local68 = local276;
                            if (local276 > 255) {
                                local68 = 255;
                            }
                        }
                    }

                    @Pc(339) float local339 = this.cameraE1_2 * (float) local84;
                    @Pc(345) float local345 = this.cameraE2_2 * (float) local84;
                    if (this.depthOverride == -1) {
                        local366 = this.cameraTx + (this.cameraE1_1 * (float) local45 + local339 + this.cameraE1_3 * (float) local55);
                        local379 = rasterizer.minX + (int) (local366 * (float) this.toolkit.projectionScaleX / local111);
                        local396 = this.cameraTy + (this.cameraE2_1 * (float) local45 + local345 + this.cameraE2_3 * (float) local55);
                        local409 = rasterizer.minY + (int) (local396 * (float) this.toolkit.projectionScaleY / local111);
                        local426 = this.cameraTx + (this.cameraE1_1 * (float) local50 + local339 + this.cameraE1_3 * (float) local55);
                        local439 = rasterizer.minX + (int) (local426 * (float) this.toolkit.projectionScaleX / local137);
                        local456 = this.cameraTy + (this.cameraE2_1 * (float) local50 + local345 + this.cameraE2_3 * (float) local55);
                        local469 = rasterizer.minY + (int) (local456 * (float) this.toolkit.projectionScaleY / local137);
                        local486 = this.cameraTx + (this.cameraE1_1 * (float) local50 + local339 + this.cameraE1_3 * (float) local60);
                        local499 = rasterizer.minX + (int) (local486 * (float) this.toolkit.projectionScaleX / local163);
                        local516 = this.cameraTy + (this.cameraE2_1 * (float) local50 + local345 + this.cameraE2_3 * (float) local60);
                        local529 = rasterizer.minY + (int) (local516 * (float) this.toolkit.projectionScaleY / local163);
                        local546 = this.cameraTx + (this.cameraE1_1 * (float) local45 + local339 + this.cameraE1_3 * (float) local60);
                        local559 = rasterizer.minX + (int) (local546 * (float) this.toolkit.projectionScaleX / local189);
                        local576 = this.cameraTy + (this.cameraE2_1 * (float) local45 + local345 + this.cameraE2_3 * (float) local60);
                        local589 = rasterizer.minY + (int) (local576 * (float) this.toolkit.projectionScaleY / local189);
                    } else {
                        local366 = this.cameraTx + (this.cameraE1_1 * (float) local45 + local339 + this.cameraE1_3 * (float) local55);
                        local379 = rasterizer.minX + (int) (local366 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        local396 = this.cameraTy + (this.cameraE2_1 * (float) local45 + local345 + this.cameraE2_3 * (float) local55);
                        local409 = rasterizer.minY + (int) (local396 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                        local426 = this.cameraTx + (this.cameraE1_1 * (float) local50 + local339 + this.cameraE1_3 * (float) local55);
                        local439 = rasterizer.minX + (int) (local426 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        local456 = this.cameraTy + (this.cameraE2_1 * (float) local50 + local345 + this.cameraE2_3 * (float) local55);
                        local469 = rasterizer.minY + (int) (local456 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                        local486 = this.cameraTx + (this.cameraE1_1 * (float) local50 + local339 + this.cameraE1_3 * (float) local60);
                        local499 = rasterizer.minX + (int) (local486 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        local516 = this.cameraTy + (this.cameraE2_1 * (float) local50 + local345 + this.cameraE2_3 * (float) local60);
                        local529 = rasterizer.minY + (int) (local516 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                        local546 = this.cameraTx + (this.cameraE1_1 * (float) local45 + local339 + this.cameraE1_3 * (float) local60);
                        local559 = rasterizer.minX + (int) (local546 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        local576 = this.cameraTy + (this.cameraE2_1 * (float) local45 + local345 + this.cameraE2_3 * (float) local60);
                        local589 = rasterizer.minY + (int) (local576 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                    }
                } else {
                    local84 = super.tileHeights[x][z];
                    local863 = super.tileHeights[x + 1][z];
                    local874 = super.tileHeights[x + 1][z + 1];
                    @Pc(883) int local883 = super.tileHeights[x][z + 1];

                    if (this.depthOverride == -1) {
                        local111 = this.cameraTz + (this.cameraE3_1 * (float) local45 + this.cameraE3_2 * (float) local84 + this.cameraE3_3 * (float) local55);
                        if (local111 <= (float) this.toolkit.zNear) {
                            return;
                        }
                        local137 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local863 + this.cameraE3_3 * (float) local55);
                        if (local137 <= (float) this.toolkit.zNear) {
                            return;
                        }
                        local163 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local874 + this.cameraE3_3 * (float) local60);
                        if (local163 <= (float) this.toolkit.zNear) {
                            return;
                        }
                        local189 = this.cameraTz + (this.cameraE3_1 * (float) local45 + this.cameraE3_2 * (float) local883 + this.cameraE3_3 * (float) local60);
                        if (local189 <= (float) this.toolkit.zNear) {
                            return;
                        }
                    } else {
                        local111 = this.cameraTz + (this.cameraE3_1 * (float) local45 + this.cameraE3_2 * (float) local84 + this.cameraE3_3 * (float) local55);
                        local137 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local863 + this.cameraE3_3 * (float) local55);
                        local163 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local874 + this.cameraE3_3 * (float) local60);
                        local189 = this.cameraTz + (this.cameraE3_1 * (float) local45 + this.cameraE3_2 * (float) local883 + this.cameraE3_3 * (float) local60);
                    }

                    if (water) {
                        local276 = (int) (local111 - (float) resource.fogPlane);
                        if (local276 > 255) {
                            local276 = 255;
                        }
                        @Pc(1116) int local1116;
                        if (local276 > 0) {
                            local62 = local276;
                            local1116 = simpleTile.waterDepthSw * local276 / 255;
                            if (local1116 > 0) {
                                local84 -= local1116;
                            }
                        }
                        local276 = (int) (local137 - (float) resource.fogPlane);
                        if (local276 > 255) {
                            local276 = 255;
                        }
                        if (local276 > 0) {
                            local64 = local276;
                            local1116 = simpleTile.waterDepthSe * local276 / 255;
                            if (local1116 > 0) {
                                local863 -= local1116;
                            }
                        }
                        local276 = (int) (local163 - (float) resource.fogPlane);
                        if (local276 > 255) {
                            local276 = 255;
                        }
                        if (local276 > 0) {
                            local66 = local276;
                            local1116 = simpleTile.waterDepthNe * local276 / 255;
                            if (local1116 > 0) {
                                local874 -= local1116;
                            }
                        }
                        local276 = (int) (local189 - (float) resource.fogPlane);
                        if (local276 > 255) {
                            local276 = 255;
                        }
                        if (local276 > 0) {
                            local68 = local276;
                            local1116 = simpleTile.waterDepthNw * local276 / 255;
                            if (local1116 > 0) {
                                local883 -= local1116;
                            }
                        }
                    } else if (resource.fogActive) {
                        local276 = (int) (local111 - (float) resource.fogPlane);
                        if (local276 > 0) {
                            local62 = local276;
                            if (local276 > 255) {
                                local62 = 255;
                            }
                        }
                        local276 = (int) (local137 - (float) resource.fogPlane);
                        if (local276 > 0) {
                            local64 = local276;
                            if (local276 > 255) {
                                local64 = 255;
                            }
                        }
                        local276 = (int) (local163 - (float) resource.fogPlane);
                        if (local276 > 0) {
                            local66 = local276;
                            if (local276 > 255) {
                                local66 = 255;
                            }
                        }
                        local276 = (int) (local189 - (float) resource.fogPlane);
                        if (local276 > 0) {
                            local68 = local276;
                            if (local276 > 255) {
                                local68 = 255;
                            }
                        }
                    }

                    if (this.depthOverride == -1) {
                        local366 = this.cameraTx + (this.cameraE1_1 * (float) local45 + this.cameraE1_2 * (float) local84 + this.cameraE1_3 * (float) local55);
                        local379 = rasterizer.minX + (int) (local366 * (float) this.toolkit.projectionScaleX / local111);
                        local396 = this.cameraTy + (this.cameraE2_1 * (float) local45 + this.cameraE2_2 * (float) local84 + this.cameraE2_3 * (float) local55);
                        local409 = rasterizer.minY + (int) (local396 * (float) this.toolkit.projectionScaleY / local111);
                        local426 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local863 + this.cameraE1_3 * (float) local55);
                        local439 = rasterizer.minX + (int) (local426 * (float) this.toolkit.projectionScaleX / local137);
                        local456 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local863 + this.cameraE2_3 * (float) local55);
                        local469 = rasterizer.minY + (int) (local456 * (float) this.toolkit.projectionScaleY / local137);
                        local486 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local874 + this.cameraE1_3 * (float) local60);
                        local499 = rasterizer.minX + (int) (local486 * (float) this.toolkit.projectionScaleX / local163);
                        local516 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local874 + this.cameraE2_3 * (float) local60);
                        local529 = rasterizer.minY + (int) (local516 * (float) this.toolkit.projectionScaleY / local163);
                        local546 = this.cameraTx + (this.cameraE1_1 * (float) local45 + this.cameraE1_2 * (float) local883 + this.cameraE1_3 * (float) local60);
                        local559 = rasterizer.minX + (int) (local546 * (float) this.toolkit.projectionScaleX / local189);
                        local576 = this.cameraTy + (this.cameraE2_1 * (float) local45 + this.cameraE2_2 * (float) local883 + this.cameraE2_3 * (float) local60);
                        local589 = rasterizer.minY + (int) (local576 * (float) this.toolkit.projectionScaleY / local189);
                    } else {
                        local366 = this.cameraTx + (this.cameraE1_1 * (float) local45 + this.cameraE1_2 * (float) local84 + this.cameraE1_3 * (float) local55);
                        local379 = rasterizer.minX + (int) (local366 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        local396 = this.cameraTy + (this.cameraE2_1 * (float) local45 + this.cameraE2_2 * (float) local84 + this.cameraE2_3 * (float) local55);
                        local409 = rasterizer.minY + (int) (local396 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                        local426 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local863 + this.cameraE1_3 * (float) local55);
                        local439 = rasterizer.minX + (int) (local426 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        local456 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local863 + this.cameraE2_3 * (float) local55);
                        local469 = rasterizer.minY + (int) (local456 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                        local486 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local874 + this.cameraE1_3 * (float) local60);
                        local499 = rasterizer.minX + (int) (local486 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        local516 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local874 + this.cameraE2_3 * (float) local60);
                        local529 = rasterizer.minY + (int) (local516 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                        local546 = this.cameraTx + (this.cameraE1_1 * (float) local45 + this.cameraE1_2 * (float) local883 + this.cameraE1_3 * (float) local60);
                        local559 = rasterizer.minX + (int) (local546 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        local576 = this.cameraTy + (this.cameraE2_1 * (float) local45 + this.cameraE2_2 * (float) local883 + this.cameraE2_3 * (float) local60);
                        local589 = rasterizer.minY + (int) (local576 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                    }
                }

                @Pc(1864) boolean local1864 = simpleTile.texture != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(simpleTile.texture).effectType);

                if (this.depthOverride == -1) {
                    local863 = local64 + local66 + local68;
                    if ((local499 - local559) * (local469 - local589) - (local529 - local589) * (local439 - local559) > 0) {
                        rasterizer.clamp = local499 < 0 || local559 < 0 || local439 < 0 || local499 > rasterizer.width || local559 > rasterizer.width || local439 > rasterizer.width;

                        if (local863 >= 765) {
                            rasterizer.renderFlatTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, (float) (int) local163, (float) (int) local189, (float) (int) local137, resource.fogColour);
                        } else if (local863 > 0) {
                            if (simpleTile.texture >= 0) {
                                local874 = -16777216;
                                if (local1864) {
                                    local874 = -1694498816;
                                }
                                rasterizer.renderTexturedTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, local163, local189, local137, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, local874 | simpleTile.colourNe & 0xFFFFFF, local874 | simpleTile.colourNw & 0xFFFFFF, local874 | simpleTile.colourSe & 0xFFFFFF, resource.fogColour, local66, local68, local64, simpleTile.texture);
                            } else {
                                if (local1864) {
                                    rasterizer.alpha = 100;
                                }
                                rasterizer.renderTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, (float) (int) local163, (float) (int) local189, (float) (int) local137, Static462.blendArgb(local66 << 24 | resource.fogColour, simpleTile.colourNe), Static462.blendArgb(local68 << 24 | resource.fogColour, simpleTile.colourNw), Static462.blendArgb(local64 << 24 | resource.fogColour, simpleTile.colourSe));
                                rasterizer.alpha = 0;
                            }
                        } else if (simpleTile.texture >= 0) {
                            local874 = -16777216;
                            if (local1864) {
                                local874 = -1694498816;
                            }
                            rasterizer.renderTexturedTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, local163, local189, local137, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, local874 | simpleTile.colourNe & 0xFFFFFF, local874 | simpleTile.colourNw & 0xFFFFFF, local874 | simpleTile.colourSe & 0xFFFFFF, 0, 0, 0, 0, simpleTile.texture);
                        } else {
                            if (local1864) {
                                rasterizer.alpha = 100;
                            }
                            rasterizer.renderTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, (float) (int) local163, (float) (int) local189, (float) (int) local137, simpleTile.colourNe, simpleTile.colourNw, simpleTile.colourSe);
                            rasterizer.alpha = 0;
                        }
                    }

                    local863 = local62 + local64 + local68;

                    if ((local379 - local439) * (local589 - local469) - (local409 - local469) * (local559 - local439) > 0) {
                        rasterizer.clamp = local379 < 0 || local439 < 0 || local559 < 0 || local379 > rasterizer.width || local439 > rasterizer.width || local559 > rasterizer.width;

                        if (local863 >= 765) {
                            rasterizer.renderFlatTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, (float) (int) local111, (float) (int) local137, (float) (int) local189, resource.fogColour);
                        } else {
                            if (local1864) {
                                rasterizer.alpha = -1694498816;
                            }

                            if (local863 > 0) {
                                if (simpleTile.texture >= 0) {
                                    local874 = -16777216;
                                    if (local1864) {
                                        local874 = -1694498816;
                                    }

                                    rasterizer.renderTexturedTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, local111, local137, local189, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, local874 | simpleTile.colourSw & 0xFFFFFF, local874 | simpleTile.colourSe & 0xFFFFFF, local874 | simpleTile.colourNw & 0xFFFFFF, resource.fogColour, local62, local64, local68, simpleTile.texture);
                                } else {
                                    if (local1864) {
                                        rasterizer.alpha = 100;
                                    }

                                    rasterizer.renderTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, (float) (int) local111, (float) (int) local137, (float) (int) local189, Static462.blendArgb(local62 << 24 | resource.fogColour, simpleTile.colourSw), Static462.blendArgb(local64 << 24 | resource.fogColour, simpleTile.colourSe), Static462.blendArgb(local68 << 24 | resource.fogColour, simpleTile.colourNw));
                                    rasterizer.alpha = 0;
                                }
                            } else if (simpleTile.texture >= 0) {
                                local874 = -16777216;
                                if (local1864) {
                                    local874 = -1694498816;
                                }

                                rasterizer.renderTexturedTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, local111, local137, local189, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, local874 | simpleTile.colourSw & 0xFFFFFF, local874 | simpleTile.colourSe & 0xFFFFFF, local874 | simpleTile.colourNw & 0xFFFFFF, 0, 0, 0, 0, simpleTile.texture);
                            } else {
                                if (local1864) {
                                    rasterizer.alpha = 100;
                                }

                                rasterizer.renderTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, (float) (int) local111, (float) (int) local137, (float) (int) local189, simpleTile.colourSw, simpleTile.colourSe, simpleTile.colourNw);
                                rasterizer.alpha = 0;
                            }
                        }
                    }
                } else {
                    local863 = local64 + local66 + local68;
                    if ((local499 - local559) * (local469 - local589) - (local529 - local589) * (local439 - local559) > 0) {
                        rasterizer.clamp = local499 < 0 || local559 < 0 || local439 < 0 || local499 > rasterizer.width || local559 > rasterizer.width || local439 > rasterizer.width;

                        if (local863 >= 765) {
                            rasterizer.renderFlatTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, (float) (int) local163, (float) (int) local189, (float) (int) local137, resource.fogColour);
                        } else {
                            if (local1864) {
                                rasterizer.alpha = -1694498816;
                            }

                            if (local863 > 0) {
                                if (simpleTile.texture >= 0) {
                                    local874 = -16777216;
                                    if (local1864) {
                                        local874 = -1694498816;
                                    }

                                    rasterizer.renderTexturedTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, local163, local189, local137, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, local874 | simpleTile.colourNe & 0xFFFFFF, local874 | simpleTile.colourNw & 0xFFFFFF, local874 | simpleTile.colourSe & 0xFFFFFF, resource.fogColour, local66, local68, local64, simpleTile.texture);
                                } else {
                                    if (local1864) {
                                        rasterizer.alpha = 100;
                                    }

                                    rasterizer.renderTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, (float) (int) local163, (float) (int) local189, (float) (int) local137, Static462.blendArgb(local66 << 24 | resource.fogColour, simpleTile.colourNe), Static462.blendArgb(local68 << 24 | resource.fogColour, simpleTile.colourNw), Static462.blendArgb(local64 << 24 | resource.fogColour, simpleTile.colourSe));
                                    rasterizer.alpha = 0;
                                }
                            } else if (simpleTile.texture >= 0) {
                                local874 = -16777216;
                                if (local1864) {
                                    local874 = -1694498816;
                                }

                                rasterizer.renderTexturedTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, local163, local189, local137, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, local874 | simpleTile.colourNe & 0xFFFFFF, local874 | simpleTile.colourNw & 0xFFFFFF, local874 | simpleTile.colourSe & 0xFFFFFF, 0, 0, 0, 0, simpleTile.texture);
                            } else {
                                if (local1864) {
                                    rasterizer.alpha = 100;
                                }

                                rasterizer.renderTriangleRgb((float) local529, (float) local589, (float) local469, (float) local499, (float) local559, (float) local439, (float) (int) local163, (float) (int) local189, (float) (int) local137, simpleTile.colourNe, simpleTile.colourNw, simpleTile.colourSe);
                                rasterizer.alpha = 0;
                            }
                        }
                    }

                    local863 = local62 + local64 + local68;

                    if ((local379 - local439) * (local589 - local469) - (local409 - local469) * (local559 - local439) > 0) {
                        rasterizer.clamp = local379 < 0 || local439 < 0 || local559 < 0 || local379 > rasterizer.width || local439 > rasterizer.width || local559 > rasterizer.width;

                        if (local863 >= 765) {
                            rasterizer.renderFlatTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, (float) (int) local111, (float) (int) local137, (float) (int) local189, resource.fogColour);
                        } else {
                            if (local1864) {
                                rasterizer.alpha = -1694498816;
                            }

                            if (local863 > 0) {
                                if (simpleTile.texture >= 0) {
                                    local874 = -16777216;
                                    if (local1864) {
                                        local874 = -1694498816;
                                    }

                                    rasterizer.renderTexturedTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, local111, local137, local189, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, local874 | simpleTile.colourSw & 0xFFFFFF, local874 | simpleTile.colourSe & 0xFFFFFF, local874 | simpleTile.colourNw & 0xFFFFFF, resource.fogColour, local62, local64, local68, simpleTile.texture);
                                } else {
                                    if (local1864) {
                                        rasterizer.alpha = 100;
                                    }

                                    rasterizer.renderTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, (float) (int) local111, (float) (int) local137, (float) (int) local189, Static462.blendArgb(local62 << 24 | resource.fogColour, simpleTile.colourSw), Static462.blendArgb(local64 << 24 | resource.fogColour, simpleTile.colourSe), Static462.blendArgb(local68 << 24 | resource.fogColour, simpleTile.colourNw));
                                    rasterizer.alpha = 0;
                                }
                            } else if (simpleTile.texture >= 0) {
                                local874 = -16777216;
                                if (local1864) {
                                    local874 = -1694498816;
                                }

                                rasterizer.renderTexturedTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, local111, local137, local189, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, local874 | simpleTile.colourSw & 0xFFFFFF, local874 | simpleTile.colourSe & 0xFFFFFF, local874 | simpleTile.colourNw & 0xFFFFFF, 0, 0, 0, 0, simpleTile.texture);
                            } else {
                                if (local1864) {
                                    rasterizer.alpha = 100;
                                }

                                rasterizer.renderTriangleRgb((float) local409, (float) local469, (float) local589, (float) local379, (float) local439, (float) local559, (float) (int) local111, (float) (int) local137, (float) (int) local189, simpleTile.colourSw, simpleTile.colourSe, simpleTile.colourNw);
                                rasterizer.alpha = 0;
                            }
                        }
                    }
                }
            }
        } else {
            @Pc(3142) JavaComplexBlendedTile complexTile = this.complexBlendedTiles[x][z];

            if (complexTile != null) {
                if (skipFlags != 0) {
                    if ((complexTile.flags & 0x4) == 0) {
                        if ((skipFlags & 0x2) != 0) {
                            return;
                        }
                    } else if ((skipFlags & 0x1) != 0) {
                        return;
                    }
                }

                @Pc(3321) float local3321;
                @Pc(3342) float local3342;
                @Pc(3222) float local3222;
                if (this.depthOverride == -1) {
                    for (local559 = 0; local559 < complexTile.vertexCount; local559++) {
                        local50 = complexTile.verticesX[local559] + (x << super.tileSizeShift);
                        local55 = complexTile.verticesY[local559];
                        local60 = complexTile.verticesZ[local559] + (z << super.tileSizeShift);
                        local3222 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local55 + this.cameraE3_3 * (float) local60);
                        if (local3222 <= (float) this.toolkit.zNear) {
                            return;
                        }

                        fogLevels[local559] = 0;
                        if (water) {
                            local409 = (int) (local3222 - (float) resource.fogPlane);
                            if (local409 > 255) {
                                local409 = 255;
                            }

                            if (local409 > 0) {
                                fogLevels[local559] = local409;
                                local469 = complexTile.waterDepths[local559] * local409 / 255;

                                if (local469 > 0) {
                                    local55 -= local469;
                                }
                            }
                        } else if (resource.fogActive) {
                            local409 = (int) (local3222 - (float) resource.fogPlane);

                            if (local409 > 0) {
                                fogLevels[local559] = local409;

                                if (fogLevels[local559] > 255) {
                                    fogLevels[local559] = 255;
                                }
                            }
                        }

                        local3321 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local55 + this.cameraE1_3 * (float) local60);
                        local3342 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local55 + this.cameraE2_3 * (float) local60);
                        screenX[local559] = rasterizer.minX + (int) (local3321 * (float) this.toolkit.projectionScaleX / local3222);
                        screenY[local559] = rasterizer.minY + (int) (local3342 * (float) this.toolkit.projectionScaleY / local3222);
                        depths[local559] = (int) local3222;
                    }
                } else {
                    for (local559 = 0; local559 < complexTile.vertexCount; local559++) {
                        local50 = complexTile.verticesX[local559] + (x << super.tileSizeShift);
                        local55 = complexTile.verticesY[local559];
                        local60 = complexTile.verticesZ[local559] + (z << super.tileSizeShift);
                        local3222 = this.cameraTz + (this.cameraE3_1 * (float) local50 + this.cameraE3_2 * (float) local55 + this.cameraE3_3 * (float) local60);
                        fogLevels[local559] = 0;

                        if (water) {
                            local409 = this.depthOverride - resource.fogPlane;
                            if (local409 > 255) {
                                local409 = 255;
                            }
                            if (local409 > 0) {
                                fogLevels[local559] = local409;
                                local469 = complexTile.waterDepths[local559] * local409 / 255;
                                if (local469 > 0) {
                                    local55 -= local469;
                                }
                            }
                        } else if (resource.fogActive) {
                            local409 = this.depthOverride - resource.fogPlane;
                            if (local409 > 0) {
                                fogLevels[local559] = local409;
                                if (fogLevels[local559] > 255) {
                                    fogLevels[local559] = 255;
                                }
                            }
                        }

                        local3321 = this.cameraTx + (this.cameraE1_1 * (float) local50 + this.cameraE1_2 * (float) local55 + this.cameraE1_3 * (float) local60);
                        local3342 = this.cameraTy + (this.cameraE2_1 * (float) local50 + this.cameraE2_2 * (float) local55 + this.cameraE2_3 * (float) local60);
                        screenX[local559] = rasterizer.minX + (int) (local3321 * (float) this.toolkit.projectionScaleX / (float) this.depthOverride);
                        screenY[local559] = rasterizer.minY + (int) (local3342 * (float) this.toolkit.projectionScaleY / (float) this.depthOverride);
                        depths[local559] = (int) local3222;
                    }
                }

                if (complexTile.faceTextures == null) {
                    for (local559 = 0; local559 < complexTile.faceCount; local559++) {
                        local409 = local559 * 3;
                        local469 = local409 + 1;
                        local529 = local469 + 1;
                        local589 = screenX[local409];
                        @Pc(3620) int local3620 = screenX[local469];
                        @Pc(3624) int local3624 = screenX[local529];
                        @Pc(3628) int local3628 = screenY[local409];
                        @Pc(3632) int local3632 = screenY[local469];
                        @Pc(3636) int local3636 = screenY[local529];
                        @Pc(3648) int local3648 = fogLevels[local409] + fogLevels[local469] + fogLevels[local529];
                        if ((local589 - local3620) * (local3636 - local3632) - (local3628 - local3632) * (local3624 - local3620) > 0) {
                            rasterizer.clamp = local589 < 0 || local3620 < 0 || local3624 < 0 || local589 > rasterizer.width || local3620 > rasterizer.width || local3624 > rasterizer.width;

                            if (local3648 >= 765) {
                                rasterizer.renderFlatTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], resource.fogColour);
                            } else if (local3648 > 0) {
                                if ((complexTile.vertexColours[local409] & 0xFFFFFF) != 0) {
                                    rasterizer.renderTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], Static572.lerpRgb(resource.fogColour, complexTile.vertexColours[local409], fogLevels[local409]), Static572.lerpRgb(resource.fogColour, complexTile.vertexColours[local469], fogLevels[local469]), Static572.lerpRgb(resource.fogColour, complexTile.vertexColours[local529], fogLevels[local529]));
                                }
                            } else if ((complexTile.vertexColours[local409] & 0xFFFFFF) != 0) {
                                rasterizer.renderTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], complexTile.vertexColours[local409], complexTile.vertexColours[local469], complexTile.vertexColours[local529]);
                            }
                        }
                    }
                } else if (this.depthOverride == -1) {
                    for (local559 = 0; local559 < complexTile.faceCount; local559++) {
                        local409 = local559 * 3;
                        local469 = local409 + 1;
                        local529 = local469 + 1;
                        local589 = screenX[local409];
                        @Pc(3620) int local3620 = screenX[local469];
                        @Pc(3624) int local3624 = screenX[local529];
                        @Pc(3628) int local3628 = screenY[local409];
                        @Pc(3632) int local3632 = screenY[local469];
                        @Pc(3636) int local3636 = screenY[local529];
                        @Pc(3648) int local3648 = fogLevels[local409] + fogLevels[local469] + fogLevels[local529];
                        if ((local589 - local3620) * (local3636 - local3632) - (local3628 - local3632) * (local3624 - local3620) > 0) {
                            rasterizer.clamp = local589 < 0 || local3620 < 0 || local3624 < 0 || local589 > rasterizer.width || local3620 > rasterizer.width || local3624 > rasterizer.width;
                            @Pc(3705) short local3705 = complexTile.faceTextures[local559];

                            if (local3648 >= 765) {
                                rasterizer.renderFlatTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], resource.fogColour);
                            } else {
                                if (local3648 > 0) {
                                    if (local3705 != -1) {
                                        @Pc(3719) int local3719 = -16777216;
                                        if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                            local3719 = -1694498816;
                                        }

                                        rasterizer.renderTexturedTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], (float) complexTile.verticesX[local409] / (float) super.tileSize, (float) complexTile.verticesX[local469] / (float) super.tileSize, (float) complexTile.verticesX[local529] / (float) super.tileSize, (float) complexTile.verticesZ[local409] / (float) super.tileSize, (float) complexTile.verticesZ[local469] / (float) super.tileSize, (float) complexTile.verticesZ[local529] / (float) super.tileSize, local3719 | complexTile.vertexColours[local409] & 0xFFFFFF, local3719 | complexTile.vertexColours[local469] & 0xFFFFFF, local3719 | complexTile.vertexColours[local529] & 0xFFFFFF, resource.fogColour, fogLevels[local409], fogLevels[local469], fogLevels[local529], local3705);
                                    } else if ((complexTile.vertexColours[local409] & 0xFFFFFF) != 0) {
                                        if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                            rasterizer.alpha = -1694498816;
                                        }

                                        rasterizer.renderTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], Static462.blendArgb(fogLevels[local409] << 24 | resource.fogColour, complexTile.vertexColours[local409]), Static462.blendArgb(fogLevels[local469] << 24 | resource.fogColour, complexTile.vertexColours[local469]), Static462.blendArgb(fogLevels[local529] << 24 | resource.fogColour, complexTile.vertexColours[local529]));
                                        rasterizer.alpha = 0;
                                    }
                                } else if (local3705 != -1) {
                                    @Pc(3719) int local3719 = -16777216;
                                    if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                        local3719 = -1694498816;
                                    }

                                    rasterizer.renderTexturedTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], (float) complexTile.verticesX[local409] / (float) super.tileSize, (float) complexTile.verticesX[local469] / (float) super.tileSize, (float) complexTile.verticesX[local529] / (float) super.tileSize, (float) complexTile.verticesZ[local409] / (float) super.tileSize, (float) complexTile.verticesZ[local469] / (float) super.tileSize, (float) complexTile.verticesZ[local529] / (float) super.tileSize, local3719 | complexTile.vertexColours[local409] & 0xFFFFFF, local3719 | complexTile.vertexColours[local469] & 0xFFFFFF, local3719 | complexTile.vertexColours[local529] & 0xFFFFFF, 0, 0, 0, 0, local3705);
                                } else if ((complexTile.vertexColours[local409] & 0xFFFFFF) != 0) {
                                    if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                        rasterizer.alpha = -1694498816;
                                    }

                                    rasterizer.renderTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], complexTile.vertexColours[local409], complexTile.vertexColours[local469], complexTile.vertexColours[local529]);
                                    rasterizer.alpha = 0;
                                }
                            }
                        }
                    }
                } else {
                    for (local559 = 0; local559 < complexTile.faceCount; local559++) {
                        local409 = local559 * 3;
                        local469 = local409 + 1;
                        local529 = local469 + 1;
                        local589 = screenX[local409];
                        @Pc(3620) int local3620 = screenX[local469];
                        @Pc(3624) int local3624 = screenX[local529];
                        @Pc(3628) int local3628 = screenY[local409];
                        @Pc(3632) int local3632 = screenY[local469];
                        @Pc(3636) int local3636 = screenY[local529];
                        @Pc(3648) int local3648 = fogLevels[local409] + fogLevels[local469] + fogLevels[local529];
                        if ((local589 - local3620) * (local3636 - local3632) - (local3628 - local3632) * (local3624 - local3620) > 0) {
                            rasterizer.clamp = local589 < 0 || local3620 < 0 || local3624 < 0 || local589 > rasterizer.width || local3620 > rasterizer.width || local3624 > rasterizer.width;
                            @Pc(3705) short local3705 = complexTile.faceTextures[local559];

                            if (local3648 >= 765) {
                                rasterizer.renderFlatTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], resource.fogColour);
                            } else {
                                if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                    rasterizer.alpha = -1694498816;
                                }

                                if (local3648 > 0) {
                                    if (local3705 != -1) {
                                        @Pc(3719) int local3719 = -16777216;
                                        if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                            local3719 = -1694498816;
                                        }

                                        rasterizer.renderTexturedTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], (float) complexTile.verticesX[local409] / (float) super.tileSize, (float) complexTile.verticesX[local469] / (float) super.tileSize, (float) complexTile.verticesX[local529] / (float) super.tileSize, (float) complexTile.verticesZ[local409] / (float) super.tileSize, (float) complexTile.verticesZ[local469] / (float) super.tileSize, (float) complexTile.verticesZ[local529] / (float) super.tileSize, local3719 | complexTile.vertexColours[local409] & 0xFFFFFF, local3719 | complexTile.vertexColours[local469] & 0xFFFFFF, local3719 | complexTile.vertexColours[local529] & 0xFFFFFF, resource.fogColour, fogLevels[local409], fogLevels[local469], fogLevels[local529], local3705);
                                    } else if ((complexTile.vertexColours[local409] & 0xFFFFFF) != 0) {
                                        if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                            rasterizer.alpha = -1694498816;
                                        }

                                        rasterizer.renderTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], Static462.blendArgb(fogLevels[local409] << 24 | resource.fogColour, complexTile.vertexColours[local409]), Static462.blendArgb(fogLevels[local469] << 24 | resource.fogColour, complexTile.vertexColours[local469]), Static462.blendArgb(fogLevels[local529] << 24 | resource.fogColour, complexTile.vertexColours[local529]));
                                        rasterizer.alpha = 0;
                                    }
                                } else if (local3705 != -1) {
                                    @Pc(3719) int local3719 = -16777216;
                                    if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                        local3719 = -1694498816;
                                    }

                                    rasterizer.renderTexturedTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], (float) complexTile.verticesX[local409] / (float) super.tileSize, (float) complexTile.verticesX[local469] / (float) super.tileSize, (float) complexTile.verticesX[local529] / (float) super.tileSize, (float) complexTile.verticesZ[local409] / (float) super.tileSize, (float) complexTile.verticesZ[local469] / (float) super.tileSize, (float) complexTile.verticesZ[local529] / (float) super.tileSize, local3719 | complexTile.vertexColours[local409] & 0xFFFFFF, local3719 | complexTile.vertexColours[local469] & 0xFFFFFF, local3719 | complexTile.vertexColours[local529] & 0xFFFFFF, 0, 0, 0, 0, local3705);
                                } else if ((complexTile.vertexColours[local409] & 0xFFFFFF) != 0) {
                                    if (local3705 != -1 && this.isWaterEffect(this.toolkit.textureSource.getMetrics(local3705).effectType)) {
                                        rasterizer.alpha = -1694498816;
                                    }

                                    rasterizer.renderTriangleRgb((float) local3628, (float) local3632, (float) local3636, (float) local589, (float) local3620, (float) local3624, (float) depths[local409], (float) depths[local469], (float) depths[local529], complexTile.vertexColours[local409], complexTile.vertexColours[local469], complexTile.vertexColours[local529]);
                                    rasterizer.alpha = 0;
                                }

                                rasterizer.alpha = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    @OriginalMember(owner = "client!qs", name = "c", descriptor = "(III)V")
    public void renderTile(@OriginalArg(0) int x, @OriginalArg(1) int z, @OriginalArg(2) int skipFlags) {
        @Pc(4) JavaThreadResource local4 = this.toolkit.threadResource(Thread.currentThread());
        local4.rasterizer.alpha = 0;
        if (this.simpleBlendedTiles != null) {
            this.renderBlendedTile(x, z, local4.water, local4, local4.rasterizer, local4.screenX, local4.screenY, local4.depths, local4.fogLevels, skipFlags);
        } else if (this.simpleTiles != null) {
            this.renderUnblendedTile(x, z, local4.rasterizer, local4.screenX, local4.screenY, local4.depths, local4.fogLevels, skipFlags);
        } else if (this.genericBlendedTiles != null) {
            this.renderGenericBlendedTile(x, z, local4.water, local4, local4.rasterizer, local4.screenX, local4.screenY, local4.depths, local4.fogLevels, skipFlags);
        }
    }

    @OriginalMember(owner = "client!qs", name = "fa", descriptor = "(IILclient!r;)Lclient!r;")
    @Override
    public Shadow fa(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) Shadow arg2) {
        return null;
    }
}
