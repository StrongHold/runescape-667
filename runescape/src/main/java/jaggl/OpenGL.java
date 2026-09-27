package jaggl;

import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.awt.Canvas;
import java.util.Hashtable;

@OriginalClass("client!jaggl/OpenGL")
public final class OpenGL {

    public static final int GL_POINTS = 0x0;

    public static final int GL_LINES = 0x1;

    public static final int GL_ONE = 0x1;

    public static final int GL_LINE_LOOP = 0x2;

    public static final int GL_TRIANGLES = 0x4;

    public static final int GL_TRIANGLE_FAN = 0x6;

    public static final int GL_QUADS = 0x7;

    public static final int GL_DEPTH_BUFFER_BIT = 0x100;

    public static final int GL_LEQUAL = 0x203;

    public static final int GL_GREATER = 0x204;

    public static final int GL_SRC_COLOR = 0x300;

    public static final int GL_ONE_MINUS_SRC_COLOR = 0x301;

    public static final int GL_SRC_ALPHA = 0x302;

    public static final int GL_ONE_MINUS_SRC_ALPHA = 0x303;

    public static final int GL_DST_COLOR = 0x306;

    public static final int GL_FRONT = 0x404;

    public static final int GL_BACK = 0x405;

    public static final int GL_VIEWPORT_BIT = 0x800;

    public static final int GL_POINT_SMOOTH = 0xB10;

    public static final int GL_POINT_SIZE_RANGE = 0xB12;

    public static final int GL_CULL_FACE = 0xB44;

    public static final int GL_LIGHTING = 0xB50;

    public static final int GL_LIGHT_MODEL_AMBIENT = 0xB53;

    public static final int GL_COLOR_MATERIAL = 0xB57;

    public static final int GL_FOG = 0xB60;

    public static final int GL_FOG_DENSITY = 0xB62;

    public static final int GL_FOG_START = 0xB63;

    public static final int GL_FOG_END = 0xB64;

    public static final int GL_FOG_MODE = 0xB65;

    public static final int GL_FOG_COLOR = 0xB66;

    public static final int GL_DEPTH_TEST = 0xB71;

    public static final int GL_MODELVIEW_MATRIX = 0xBA6;

    public static final int GL_ALPHA_TEST = 0xBC0;

    public static final int GL_BLEND = 0xBE2;

    public static final int GL_SCISSOR_TEST = 0xC11;

    public static final int GL_FOG_HINT = 0xC54;

    public static final int GL_TEXTURE_GEN_S = 0xC60;

    public static final int GL_TEXTURE_GEN_T = 0xC61;

    public static final int GL_TEXTURE_GEN_R = 0xC62;

    public static final int GL_TEXTURE_GEN_Q = 0xC63;

    public static final int GL_UNPACK_ROW_LENGTH = 0xCF2;

    public static final int GL_UNPACK_ALIGNMENT = 0xCF5;

    public static final int GL_RED_SCALE = 0xD14;

    public static final int GL_RED_BIAS = 0xD15;

    public static final int GL_GREEN_SCALE = 0xD18;

    public static final int GL_GREEN_BIAS = 0xD19;

    public static final int GL_BLUE_SCALE = 0xD1A;

    public static final int GL_BLUE_BIAS = 0xD1B;

    public static final int GL_TEXTURE_2D = 0xDE1;

    public static final int GL_FASTEST = 0x1101;

    public static final int GL_AMBIENT = 0x1200;

    public static final int GL_DIFFUSE = 0x1201;

    public static final int GL_POSITION = 0x1203;

    public static final int GL_CONSTANT_ATTENUATION = 0x1207;

    public static final int GL_LINEAR_ATTENUATION = 0x1208;

    public static final int GL_QUADRATIC_ATTENUATION = 0x1209;

    public static final int GL_COMPILE = 0x1300;

    public static final int GL_UNSIGNED_BYTE = 0x1401;

    public static final int GL_FLOAT = 0x1406;

    public static final int GL_AMBIENT_AND_DIFFUSE = 0x1602;

    public static final int GL_MODELVIEW = 0x1700;

    public static final int GL_PROJECTION = 0x1701;

    public static final int GL_TEXTURE = 0x1702;

    public static final int GL_COLOR = 0x1800;

    public static final int GL_ALPHA = 0x1906;

    public static final int GL_RGBA = 0x1908;

    public static final int GL_FILL = 0x1B02;

    public static final int GL_SMOOTH = 0x1D01;

    public static final int GL_VENDOR = 0x1F00;

    public static final int GL_RENDERER = 0x1F01;

    public static final int GL_VERSION = 0x1F02;

    public static final int GL_S = 0x2000;

    public static final int GL_T = 0x2001;

    public static final int GL_R = 0x2002;

    public static final int GL_Q = 0x2003;

    public static final int GL_MODULATE = 0x2100;

    public static final int GL_TEXTURE_ENV_MODE = 0x2200;

    public static final int GL_TEXTURE_ENV_COLOR = 0x2201;

    public static final int GL_TEXTURE_ENV = 0x2300;

    public static final int GL_EYE_LINEAR = 0x2400;

    public static final int GL_OBJECT_LINEAR = 0x2401;

    public static final int GL_TEXTURE_GEN_MODE = 0x2500;

    public static final int GL_OBJECT_PLANE = 0x2501;

    public static final int GL_EYE_PLANE = 0x2502;

    public static final int GL_NEAREST = 0x2600;

    public static final int GL_LINEAR = 0x2601;

    public static final int GL_NEAREST_MIPMAP_NEAREST = 0x2700;

    public static final int GL_LINEAR_MIPMAP_LINEAR = 0x2703;

    public static final int GL_TEXTURE_MAG_FILTER = 0x2800;

    public static final int GL_TEXTURE_MIN_FILTER = 0x2801;

    public static final int GL_TEXTURE_WRAP_S = 0x2802;

    public static final int GL_TEXTURE_WRAP_T = 0x2803;

    public static final int GL_REPEAT = 0x2901;

    public static final int GL_LIGHT0 = 0x4000;

    public static final int GL_COLOR_BUFFER_BIT = 0x4000;

    public static final int GL_LIGHT1 = 0x4001;

    public static final int GL_LIGHT2 = 0x4002;

    public static final int GL_VERTEX_ARRAY = 0x8074;

    public static final int GL_NORMAL_ARRAY = 0x8075;

    public static final int GL_COLOR_ARRAY = 0x8076;

    public static final int GL_TEXTURE_COORD_ARRAY = 0x8078;

    public static final int GL_MULTISAMPLE = 0x809D;

    public static final int GL_BGRA = 0x80E1;

    public static final int GL_CLAMP_TO_EDGE = 0x812F;

    public static final int GL_TEXTURE0 = 0x84C0;

    public static final int GL_TEXTURE1 = 0x84C1;

    public static final int GL_MAX_TEXTURE_UNITS = 0x84E2;

    public static final int GL_NORMAL_MAP = 0x8511;

    public static final int GL_REFLECTION_MAP = 0x8512;

    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;

    public static final int GL_COMBINE = 0x8570;

    public static final int GL_COMBINE_RGB = 0x8571;

    public static final int GL_COMBINE_ALPHA = 0x8572;

    public static final int GL_RGB_SCALE = 0x8573;

    public static final int GL_CONSTANT = 0x8576;

    public static final int GL_PREVIOUS = 0x8578;

    public static final int GL_SRC0_RGB = 0x8580;

    public static final int GL_SRC2_RGB = 0x8582;

    public static final int GL_SRC0_ALPHA = 0x8588;

    public static final int GL_SRC2_ALPHA = 0x858A;

    public static final int GL_OPERAND0_RGB = 0x8590;

    public static final int GL_OPERAND0_ALPHA = 0x8598;

    public static final int GL_VERTEX_PROGRAM_ARB = 0x8620;

    public static final int GL_PROGRAM_ERROR_POSITION_ARB = 0x864B;

    public static final int GL_FRAGMENT_PROGRAM_ARB = 0x8804;

    public static final int GL_RGBA16F = 0x881A;

    public static final int GL_MAX_TEXTURE_COORDS = 0x8871;

    public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872;

    public static final int GL_PROGRAM_FORMAT_ASCII_ARB = 0x8875;

    public static final int GL_ARRAY_BUFFER = 0x8892;

    public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;

    public static final int GL_COMPILE_STATUS = 0x8B81;

    public static final int GL_LINK_STATUS = 0x8B82;

    public static final int GL_INFO_LOG_LENGTH = 0x8B84;

    public static final int GL_READ_FRAMEBUFFER = 0x8CA8;

    public static final int GL_DRAW_FRAMEBUFFER = 0x8CA9;

    public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5;

    public static final int GL_FRAMEBUFFER = 0x8D40;

    public static final int GL_RENDERBUFFER = 0x8D41;

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "a", descriptor = "Ljava/util/Hashtable;")
    public static final Hashtable a = new Hashtable();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "peer", descriptor = "J")
    public long peer;

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "c", descriptor = "Ljava/util/Hashtable;")
    public Hashtable c;

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "b", descriptor = "Ljava/lang/Thread;")
    public Thread b;

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPixelZoom", descriptor = "(FF)V")
    public static native void glPixelZoom(@OriginalArg(0) float xfactor, @OriginalArg(1) float yfactor);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUniform1iARB", descriptor = "(II)V")
    public static native void glUniform1iARB(@OriginalArg(0) int location, @OriginalArg(1) int v0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glColor3f", descriptor = "(FFF)V")
    public static native void glColor3f(@OriginalArg(0) float red, @OriginalArg(1) float green, @OriginalArg(2) float blue);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBufferDataARBa", descriptor = "(IIJI)V")
    public static native void glBufferDataARBa(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) long arg2, @OriginalArg(3) int arg3);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glMultiTexCoord2i", descriptor = "(III)V")
    public static native void glMultiTexCoord2i(@OriginalArg(0) int target, @OriginalArg(1) int s, @OriginalArg(2) int t);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexCoord3f", descriptor = "(FFF)V")
    public static native void glTexCoord3f(@OriginalArg(0) float s, @OriginalArg(1) float t, @OriginalArg(2) float r);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glReadPixelsi", descriptor = "(IIIIII[II)V")
    public static native void glReadPixelsi(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int format, @OriginalArg(5) int type, @OriginalArg(6) int[] pixels, @OriginalArg(7) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glRasterPos2i", descriptor = "(II)V")
    public static native void glRasterPos2i(@OriginalArg(0) int x, @OriginalArg(1) int y);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glNormal3f", descriptor = "(FFF)V")
    public static native void glNormal3f(@OriginalArg(0) float nx, @OriginalArg(1) float ny, @OriginalArg(2) float nz);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetTexImageub", descriptor = "(IIII[BI)V")
    public static native void glGetTexImageub(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int format, @OriginalArg(3) int type, @OriginalArg(4) byte[] pixels, @OriginalArg(5) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCreateProgramObjectARB", descriptor = "()J")
    public static native long glCreateProgramObjectARB();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glVertexPointer", descriptor = "(IIIJ)V")
    public static native void glVertexPointer(@OriginalArg(0) int size, @OriginalArg(1) int type, @OriginalArg(2) int stride, @OriginalArg(3) long pointer);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glShaderSourceARB", descriptor = "(JLjava/lang/String;)V")
    public static native void glShaderSourceARB(@OriginalArg(0) long shaderObj, @OriginalArg(1) String string);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glProgramStringARB", descriptor = "(IILjava/lang/String;)V")
    public static native void glProgramStringARB(@OriginalArg(0) int target, @OriginalArg(1) int format, @OriginalArg(2) String string);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetProgramivARB", descriptor = "(II[II)V")
    public static native void glGetProgramivARB(@OriginalArg(0) int target, @OriginalArg(1) int pname, @OriginalArg(2) int[] params, @OriginalArg(3) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGenFramebuffersEXT", descriptor = "(I[II)V")
    public static native void glGenFramebuffersEXT(@OriginalArg(0) int n, @OriginalArg(1) int[] framebuffers, @OriginalArg(2) int framebuffersOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexParameteri", descriptor = "(III)V")
    public static native void glTexParameteri(@OriginalArg(0) int target, @OriginalArg(1) int pname, @OriginalArg(2) int param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDeleteBuffersARB", descriptor = "(I[II)V")
    public static native void glDeleteBuffersARB(@OriginalArg(0) int n, @OriginalArg(1) int[] buffers, @OriginalArg(2) int buffersOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDisable", descriptor = "(I)V")
    public static native void glDisable(@OriginalArg(0) int cap);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexSubImage2Di", descriptor = "(IIIIIIII[II)V")
    public static native void glTexSubImage2Di(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int xoffset, @OriginalArg(3) int yoffset, @OriginalArg(4) int width, @OriginalArg(5) int height, @OriginalArg(6) int format, @OriginalArg(7) int type, @OriginalArg(8) int[] pixels, @OriginalArg(9) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGenBuffersARB", descriptor = "(I[II)V")
    public static native void glGenBuffersARB(@OriginalArg(0) int n, @OriginalArg(1) int[] buffers, @OriginalArg(2) int buffersOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexImage2Dub", descriptor = "(IIIIIIII[BI)V")
    public static native void glTexImage2Dub(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int internalformat, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int border, @OriginalArg(6) int format, @OriginalArg(7) int type, @OriginalArg(8) byte[] pixels, @OriginalArg(9) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetInfoLogARB", descriptor = "(JI[II[BI)V")
    public static native void glGetInfoLogARB(@OriginalArg(0) long obj, @OriginalArg(1) int maxLength, @OriginalArg(2) int[] length, @OriginalArg(3) int lengthOffset, @OriginalArg(4) byte[] infoLog, @OriginalArg(5) int infoLogOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glColor4ub", descriptor = "(BBBB)V")
    public static native void glColor4ub(@OriginalArg(0) byte red, @OriginalArg(1) byte green, @OriginalArg(2) byte blue, @OriginalArg(3) byte alpha);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUniform1fARB", descriptor = "(IF)V")
    public static native void glUniform1fARB(@OriginalArg(0) int location, @OriginalArg(1) float v0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glVertex3f", descriptor = "(FFF)V")
    public static native void glVertex3f(@OriginalArg(0) float x, @OriginalArg(1) float y, @OriginalArg(2) float z);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexCoord2f", descriptor = "(FF)V")
    public static native void glTexCoord2f(@OriginalArg(0) float s, @OriginalArg(1) float t);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDeleteObjectARB", descriptor = "(J)V")
    public static native void glDeleteObjectARB(@OriginalArg(0) long obj);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDetachObjectARB", descriptor = "(JJ)V")
    public static native void glDetachObjectARB(@OriginalArg(0) long containerObj, @OriginalArg(1) long attachedObj);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glColor4f", descriptor = "(FFFF)V")
    public static native void glColor4f(@OriginalArg(0) float red, @OriginalArg(1) float green, @OriginalArg(2) float blue, @OriginalArg(3) float alpha);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexImage3Dub", descriptor = "(IIIIIIIII[BI)V")
    public static native void glTexImage3Dub(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int internalformat, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int depth, @OriginalArg(6) int border, @OriginalArg(7) int format, @OriginalArg(8) int type, @OriginalArg(9) byte[] pixels, @OriginalArg(10) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glMapBufferARB", descriptor = "(II)J")
    public static native long glMapBufferARB(@OriginalArg(0) int target, @OriginalArg(1) int access);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glEnable", descriptor = "(I)V")
    public static native void glEnable(@OriginalArg(0) int cap);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glLinkProgramARB", descriptor = "(J)V")
    public static native void glLinkProgramARB(@OriginalArg(0) long programObj);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFramebufferRenderbufferEXT", descriptor = "(IIII)V")
    public static native void glFramebufferRenderbufferEXT(@OriginalArg(0) int target, @OriginalArg(1) int attachment, @OriginalArg(2) int renderbuffertarget, @OriginalArg(3) int renderbuffer);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glColorMask", descriptor = "(ZZZZ)V")
    public static native void glColorMask(@OriginalArg(0) boolean red, @OriginalArg(1) boolean green, @OriginalArg(2) boolean blue, @OriginalArg(3) boolean alpha);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDrawPixelsub", descriptor = "(IIII[BI)V")
    public static native void glDrawPixelsub(@OriginalArg(0) int width, @OriginalArg(1) int height, @OriginalArg(2) int format, @OriginalArg(3) int type, @OriginalArg(4) byte[] pixels, @OriginalArg(5) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexImage2Di", descriptor = "(IIIIIIII[II)V")
    public static native void glTexImage2Di(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int internalformat, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int border, @OriginalArg(6) int format, @OriginalArg(7) int type, @OriginalArg(8) int[] pixels, @OriginalArg(9) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGenRenderbuffersEXT", descriptor = "(I[II)V")
    public static native void glGenRenderbuffersEXT(@OriginalArg(0) int n, @OriginalArg(1) int[] renderbuffers, @OriginalArg(2) int renderbuffersOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGenLists", descriptor = "(I)I")
    public static native int glGenLists(@OriginalArg(0) int range);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexParameterf", descriptor = "(IIF)V")
    public static native void glTexParameterf(@OriginalArg(0) int target, @OriginalArg(1) int pname, @OriginalArg(2) float param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glNewList", descriptor = "(II)V")
    public static native void glNewList(@OriginalArg(0) int list, @OriginalArg(1) int mode);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetObjectParameterivARB", descriptor = "(JI[II)V")
    public static native void glGetObjectParameterivARB(@OriginalArg(0) long obj, @OriginalArg(1) int pname, @OriginalArg(2) int[] params, @OriginalArg(3) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUseProgramObjectARB", descriptor = "(J)V")
    public static native void glUseProgramObjectARB(@OriginalArg(0) long programObj);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glEnd", descriptor = "()V")
    public static native void glEnd();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDrawBuffer", descriptor = "(I)V")
    public static native void glDrawBuffer(@OriginalArg(0) int buf);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCreateShaderObjectARB", descriptor = "(I)J")
    public static native long glCreateShaderObjectARB(@OriginalArg(0) int shaderType);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetIntegerv", descriptor = "(I[II)V")
    public static native void glGetIntegerv(@OriginalArg(0) int pname, @OriginalArg(1) int[] data, @OriginalArg(2) int dataOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glColorMaterial", descriptor = "(II)V")
    public static native void glColorMaterial(@OriginalArg(0) int face, @OriginalArg(1) int mode);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetTexImagei", descriptor = "(IIII[II)V")
    public static native void glGetTexImagei(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int format, @OriginalArg(3) int type, @OriginalArg(4) int[] pixels, @OriginalArg(5) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUniformMatrix4fvARB", descriptor = "(IIZ[FI)V")
    public static native void glUniformMatrix4fvARB(@OriginalArg(0) int location, @OriginalArg(1) int count, @OriginalArg(2) boolean transpose, @OriginalArg(3) float[] value, @OriginalArg(4) int valueOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDepthFunc", descriptor = "(I)V")
    public static native void glDepthFunc(@OriginalArg(0) int func);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDeleteTextures", descriptor = "(I[II)V")
    public static native void glDeleteTextures(@OriginalArg(0) int n, @OriginalArg(1) int[] textures, @OriginalArg(2) int texturesOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glColorPointer", descriptor = "(IIIJ)V")
    public static native void glColorPointer(@OriginalArg(0) int size, @OriginalArg(1) int type, @OriginalArg(2) int stride, @OriginalArg(3) long pointer);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFlush", descriptor = "()V")
    public static native void glFlush();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexCoord2i", descriptor = "(II)V")
    public static native void glTexCoord2i(@OriginalArg(0) int s, @OriginalArg(1) int t);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glProgramLocalParameter4fARB", descriptor = "(IIFFFF)V")
    public static native void glProgramLocalParameter4fARB(@OriginalArg(0) int target, @OriginalArg(1) int index, @OriginalArg(2) float x, @OriginalArg(3) float y, @OriginalArg(4) float z, @OriginalArg(5) float w);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDrawArrays", descriptor = "(III)V")
    public static native void glDrawArrays(@OriginalArg(0) int mode, @OriginalArg(1) int first, @OriginalArg(2) int count);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBlitFramebufferEXT", descriptor = "(IIIIIIIIII)V")
    public static native void glBlitFramebufferEXT(@OriginalArg(0) int srcX0, @OriginalArg(1) int srcY0, @OriginalArg(2) int srcX1, @OriginalArg(3) int srcY1, @OriginalArg(4) int dstX0, @OriginalArg(5) int dstY0, @OriginalArg(6) int dstX1, @OriginalArg(7) int dstY1, @OriginalArg(8) int mask, @OriginalArg(9) int filter);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDrawPixelsi", descriptor = "(IIII[II)V")
    public static native void glDrawPixelsi(@OriginalArg(0) int width, @OriginalArg(1) int height, @OriginalArg(2) int format, @OriginalArg(3) int type, @OriginalArg(4) int[] pixels, @OriginalArg(5) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexImage1Dub", descriptor = "(IIIIIII[BI)V")
    public static native void glTexImage1Dub(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int internalformat, @OriginalArg(3) int width, @OriginalArg(4) int border, @OriginalArg(5) int format, @OriginalArg(6) int type, @OriginalArg(7) byte[] pixels, @OriginalArg(8) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glAttachObjectARB", descriptor = "(JJ)V")
    public static native void glAttachObjectARB(@OriginalArg(0) long containerObj, @OriginalArg(1) long obj);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glMultMatrixf", descriptor = "([FI)V")
    public static native void glMultMatrixf(@OriginalArg(0) float[] m, @OriginalArg(1) int mOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetError", descriptor = "()I")
    public static native int glGetError();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFramebufferTexture3DEXT", descriptor = "(IIIIII)V")
    public static native void glFramebufferTexture3DEXT(@OriginalArg(0) int target, @OriginalArg(1) int attachment, @OriginalArg(2) int textarget, @OriginalArg(3) int texture, @OriginalArg(4) int level, @OriginalArg(5) int zoffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glRotatef", descriptor = "(FFFF)V")
    public static native void glRotatef(@OriginalArg(0) float angle, @OriginalArg(1) float x, @OriginalArg(2) float y, @OriginalArg(3) float z);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPushAttrib", descriptor = "(I)V")
    public static native void glPushAttrib(@OriginalArg(0) int mask);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGenerateMipmapEXT", descriptor = "(I)V")
    public static native void glGenerateMipmapEXT(@OriginalArg(0) int target);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glViewport", descriptor = "(IIII)V")
    public static native void glViewport(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGenTextures", descriptor = "(I[II)V")
    public static native void glGenTextures(@OriginalArg(0) int n, @OriginalArg(1) int[] textures, @OriginalArg(2) int texturesOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBindFramebufferEXT", descriptor = "(II)V")
    public static native void glBindFramebufferEXT(@OriginalArg(0) int target, @OriginalArg(1) int framebuffer);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPopAttrib", descriptor = "()V")
    public static native void glPopAttrib();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDeleteProgramARB", descriptor = "(I)V")
    public static native void glDeleteProgramARB(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glClientActiveTexture", descriptor = "(I)V")
    public static native void glClientActiveTexture(@OriginalArg(0) int texture);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glLightModelfv", descriptor = "(I[FI)V")
    public static native void glLightModelfv(@OriginalArg(0) int pname, @OriginalArg(1) float[] params, @OriginalArg(2) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBindRenderbufferEXT", descriptor = "(II)V")
    public static native void glBindRenderbufferEXT(@OriginalArg(0) int target, @OriginalArg(1) int renderbuffer);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDepthMask", descriptor = "(Z)V")
    public static native void glDepthMask(@OriginalArg(0) boolean flag);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexSubImage2Dub", descriptor = "(IIIIIIII[BI)V")
    public static native void glTexSubImage2Dub(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int xoffset, @OriginalArg(3) int yoffset, @OriginalArg(4) int width, @OriginalArg(5) int height, @OriginalArg(6) int format, @OriginalArg(7) int type, @OriginalArg(8) byte[] pixels, @OriginalArg(9) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glMatrixMode", descriptor = "(I)V")
    public static native void glMatrixMode(@OriginalArg(0) int mode);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBufferDataARBub", descriptor = "(II[BII)V")
    public static native void glBufferDataARBub(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) byte[] arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBindBufferARB", descriptor = "(II)V")
    public static native void glBindBufferARB(@OriginalArg(0) int target, @OriginalArg(1) int buffer);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glMaterialfv", descriptor = "(II[FI)V")
    public static native void glMaterialfv(@OriginalArg(0) int face, @OriginalArg(1) int pname, @OriginalArg(2) float[] params, @OriginalArg(3) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glLoadIdentity", descriptor = "()V")
    public static native void glLoadIdentity();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPixelStorei", descriptor = "(II)V")
    public static native void glPixelStorei(@OriginalArg(0) int pname, @OriginalArg(1) int param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glMultiTexCoord2f", descriptor = "(IFF)V")
    public static native void glMultiTexCoord2f(@OriginalArg(0) int target, @OriginalArg(1) float s, @OriginalArg(2) float t);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetFloatv", descriptor = "(I[FI)V")
    public static native void glGetFloatv(@OriginalArg(0) int pname, @OriginalArg(1) float[] data, @OriginalArg(2) int dataOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPolygonMode", descriptor = "(II)V")
    public static native void glPolygonMode(@OriginalArg(0) int face, @OriginalArg(1) int mode);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPointSize", descriptor = "(F)V")
    public static native void glPointSize(@OriginalArg(0) float size);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glRenderbufferStorageEXT", descriptor = "(IIII)V")
    public static native void glRenderbufferStorageEXT(@OriginalArg(0) int target, @OriginalArg(1) int internalformat, @OriginalArg(2) int width, @OriginalArg(3) int height);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFinish", descriptor = "()V")
    public static native void glFinish();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glScalef", descriptor = "(FFF)V")
    public static native void glScalef(@OriginalArg(0) float x, @OriginalArg(1) float y, @OriginalArg(2) float z);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCullFace", descriptor = "(I)V")
    public static native void glCullFace(@OriginalArg(0) int mode);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glShaderSourceRawARB", descriptor = "(J[B)V")
    public static native void glShaderSourceRawARB(@OriginalArg(0) long arg0, @OriginalArg(1) byte[] arg1);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexEnvfv", descriptor = "(II[FI)V")
    public static native void glTexEnvfv(@OriginalArg(0) int target, @OriginalArg(1) int pname, @OriginalArg(2) float[] params, @OriginalArg(3) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPixelTransferf", descriptor = "(IF)V")
    public static native void glPixelTransferf(@OriginalArg(0) int pname, @OriginalArg(1) float param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glMultiTexCoord3i", descriptor = "(IIII)V")
    public static native void glMultiTexCoord3i(@OriginalArg(0) int target, @OriginalArg(1) int s, @OriginalArg(2) int t, @OriginalArg(3) int r);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCallList", descriptor = "(I)V")
    public static native void glCallList(@OriginalArg(0) int list);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUniformMatrix3fvARB", descriptor = "(IIZ[FI)V")
    public static native void glUniformMatrix3fvARB(@OriginalArg(0) int location, @OriginalArg(1) int count, @OriginalArg(2) boolean transpose, @OriginalArg(3) float[] value, @OriginalArg(4) int valueOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBindProgramARB", descriptor = "(II)V")
    public static native void glBindProgramARB(@OriginalArg(0) int target, @OriginalArg(1) int program);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexEnvi", descriptor = "(III)V")
    public static native void glTexEnvi(@OriginalArg(0) int target, @OriginalArg(1) int pname, @OriginalArg(2) int param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glEndList", descriptor = "()V")
    public static native void glEndList();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCompileShaderARB", descriptor = "(J)V")
    public static native void glCompileShaderARB(@OriginalArg(0) long shaderObj);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCopyPixels", descriptor = "(IIIII)V")
    public static native void glCopyPixels(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int type);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexImage2Df", descriptor = "(IIIIIIII[FI)V")
    public static native void glTexImage2Df(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int internalformat, @OriginalArg(3) int width, @OriginalArg(4) int height, @OriginalArg(5) int border, @OriginalArg(6) int format, @OriginalArg(7) int type, @OriginalArg(8) float[] pixels, @OriginalArg(9) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUnmapBufferARB", descriptor = "(I)Z")
    public static native boolean glUnmapBufferARB(@OriginalArg(0) int target);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glClearDepth", descriptor = "(F)V")
    public static native void glClearDepth(@OriginalArg(0) float depth);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glVertex2i", descriptor = "(II)V")
    public static native void glVertex2i(@OriginalArg(0) int x, @OriginalArg(1) int y);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glClearColor", descriptor = "(FFFF)V")
    public static native void glClearColor(@OriginalArg(0) float red, @OriginalArg(1) float green, @OriginalArg(2) float blue, @OriginalArg(3) float alpha);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPushMatrix", descriptor = "()V")
    public static native void glPushMatrix();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glActiveTexture", descriptor = "(I)V")
    public static native void glActiveTexture(@OriginalArg(0) int texture);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexSubImage2Df", descriptor = "(IIIIIIII[FI)V")
    public static native void glTexSubImage2Df(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int xoffset, @OriginalArg(3) int yoffset, @OriginalArg(4) int width, @OriginalArg(5) int height, @OriginalArg(6) int format, @OriginalArg(7) int type, @OriginalArg(8) float[] pixels, @OriginalArg(9) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glScissor", descriptor = "(IIII)V")
    public static native void glScissor(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glColor3ub", descriptor = "(BBB)V")
    public static native void glColor3ub(@OriginalArg(0) byte red, @OriginalArg(1) byte green, @OriginalArg(2) byte blue);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glClear", descriptor = "(I)V")
    public static native void glClear(@OriginalArg(0) int mask);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDeleteLists", descriptor = "(II)V")
    public static native void glDeleteLists(@OriginalArg(0) int list, @OriginalArg(1) int range);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGenProgramARB", descriptor = "()I")
    public static native int glGenProgramARB();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFogfv", descriptor = "(I[FI)V")
    public static native void glFogfv(@OriginalArg(0) int pname, @OriginalArg(1) float[] params, @OriginalArg(2) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glProgramRawARB", descriptor = "(II[B)V")
    public static native void glProgramRawARB(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) byte[] arg2);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexGenfv", descriptor = "(II[FI)V")
    public static native void glTexGenfv(@OriginalArg(0) int coord, @OriginalArg(1) int pname, @OriginalArg(2) float[] params, @OriginalArg(3) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFramebufferTexture2DEXT", descriptor = "(IIIII)V")
    public static native void glFramebufferTexture2DEXT(@OriginalArg(0) int target, @OriginalArg(1) int attachment, @OriginalArg(2) int textarget, @OriginalArg(3) int texture, @OriginalArg(4) int level);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUniform4fARB", descriptor = "(IFFFF)V")
    public static native void glUniform4fARB(@OriginalArg(0) int location, @OriginalArg(1) float v0, @OriginalArg(2) float v1, @OriginalArg(3) float v2, @OriginalArg(4) float v3);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glLightfv", descriptor = "(II[FI)V")
    public static native void glLightfv(@OriginalArg(0) int light, @OriginalArg(1) int pname, @OriginalArg(2) float[] params, @OriginalArg(3) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexGeni", descriptor = "(III)V")
    public static native void glTexGeni(@OriginalArg(0) int coord, @OriginalArg(1) int pname, @OriginalArg(2) int param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDrawBuffersARB", descriptor = "(I[II)V")
    public static native void glDrawBuffersARB(@OriginalArg(0) int n, @OriginalArg(1) int[] bufs, @OriginalArg(2) int bufsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glLineWidth", descriptor = "(F)V")
    public static native void glLineWidth(@OriginalArg(0) float width);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetString", descriptor = "(I)Ljava/lang/String;")
    public static native String glGetString(@OriginalArg(0) int name);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexCoordPointer", descriptor = "(IIIJ)V")
    public static native void glTexCoordPointer(@OriginalArg(0) int size, @OriginalArg(1) int type, @OriginalArg(2) int stride, @OriginalArg(3) long pointer);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glShadeModel", descriptor = "(I)V")
    public static native void glShadeModel(@OriginalArg(0) int mode);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glLoadMatrixf", descriptor = "([FI)V")
    public static native void glLoadMatrixf(@OriginalArg(0) float[] m, @OriginalArg(1) int mOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBlendFunc", descriptor = "(II)V")
    public static native void glBlendFunc(@OriginalArg(0) int sfactor, @OriginalArg(1) int dfactor);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glGetUniformLocationARB", descriptor = "(JLjava/lang/String;)I")
    public static native int glGetUniformLocationARB(@OriginalArg(0) long programObj, @OriginalArg(1) String name);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCheckFramebufferStatusEXT", descriptor = "(I)I")
    public static native int glCheckFramebufferStatusEXT(@OriginalArg(0) int target);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUniform3fARB", descriptor = "(IFFF)V")
    public static native void glUniform3fARB(@OriginalArg(0) int location, @OriginalArg(1) float v0, @OriginalArg(2) float v1, @OriginalArg(3) float v2);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDrawElements", descriptor = "(IIIJ)V")
    public static native void glDrawElements(@OriginalArg(0) int mode, @OriginalArg(1) int count, @OriginalArg(2) int type, @OriginalArg(3) long indices);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFogf", descriptor = "(IF)V")
    public static native void glFogf(@OriginalArg(0) int pname, @OriginalArg(1) float param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexEnvf", descriptor = "(IIF)V")
    public static native void glTexEnvf(@OriginalArg(0) int target, @OriginalArg(1) int pname, @OriginalArg(2) float param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glRenderbufferStorageMultisampleEXT", descriptor = "(IIIII)V")
    public static native void glRenderbufferStorageMultisampleEXT(@OriginalArg(0) int target, @OriginalArg(1) int samples, @OriginalArg(2) int internalformat, @OriginalArg(3) int width, @OriginalArg(4) int height);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCopyTexSubImage3D", descriptor = "(IIIIIIIII)V")
    public static native void glCopyTexSubImage3D(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int xoffset, @OriginalArg(3) int yoffset, @OriginalArg(4) int zoffset, @OriginalArg(5) int x, @OriginalArg(6) int y, @OriginalArg(7) int width, @OriginalArg(8) int height);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFrustum", descriptor = "(DDDDDD)V")
    public static native void glFrustum(@OriginalArg(0) double left, @OriginalArg(1) double right, @OriginalArg(2) double bottom, @OriginalArg(3) double top, @OriginalArg(4) double zNear, @OriginalArg(5) double zFar);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUniform2fARB", descriptor = "(IFF)V")
    public static native void glUniform2fARB(@OriginalArg(0) int location, @OriginalArg(1) float v0, @OriginalArg(2) float v1);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDeleteFramebuffersEXT", descriptor = "(I[II)V")
    public static native void glDeleteFramebuffersEXT(@OriginalArg(0) int n, @OriginalArg(1) int[] framebuffers, @OriginalArg(2) int framebuffersOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glUniformMatrix2fvARB", descriptor = "(IIZ[FI)V")
    public static native void glUniformMatrix2fvARB(@OriginalArg(0) int location, @OriginalArg(1) int count, @OriginalArg(2) boolean transpose, @OriginalArg(3) float[] value, @OriginalArg(4) int valueOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCopyTexSubImage2D", descriptor = "(IIIIIIII)V")
    public static native void glCopyTexSubImage2D(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int xoffset, @OriginalArg(3) int yoffset, @OriginalArg(4) int x, @OriginalArg(5) int y, @OriginalArg(6) int width, @OriginalArg(7) int height);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glCopyTexImage2D", descriptor = "(IIIIIIII)V")
    public static native void glCopyTexImage2D(@OriginalArg(0) int target, @OriginalArg(1) int level, @OriginalArg(2) int internalformat, @OriginalArg(3) int x, @OriginalArg(4) int y, @OriginalArg(5) int width, @OriginalArg(6) int height, @OriginalArg(7) int border);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glOrtho", descriptor = "(DDDDDD)V")
    public static native void glOrtho(@OriginalArg(0) double left, @OriginalArg(1) double right, @OriginalArg(2) double bottom, @OriginalArg(3) double top, @OriginalArg(4) double zNear, @OriginalArg(5) double zFar);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBufferSubDataARBub", descriptor = "(III[BI)V")
    public static native void glBufferSubDataARBub(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) byte[] arg3, @OriginalArg(4) int arg4);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTexCoord3i", descriptor = "(III)V")
    public static native void glTexCoord3i(@OriginalArg(0) int s, @OriginalArg(1) int t, @OriginalArg(2) int r);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glTranslatef", descriptor = "(FFF)V")
    public static native void glTranslatef(@OriginalArg(0) float x, @OriginalArg(1) float y, @OriginalArg(2) float z);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glPopMatrix", descriptor = "()V")
    public static native void glPopMatrix();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glStencilFunc", descriptor = "(III)V")
    public static native void glStencilFunc(@OriginalArg(0) int func, @OriginalArg(1) int ref, @OriginalArg(2) int mask);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBegin", descriptor = "(I)V")
    public static native void glBegin(@OriginalArg(0) int mode);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBindTexture", descriptor = "(II)V")
    public static native void glBindTexture(@OriginalArg(0) int target, @OriginalArg(1) int texture);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glReadBuffer", descriptor = "(I)V")
    public static native void glReadBuffer(@OriginalArg(0) int src);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glReadPixelsub", descriptor = "(IIIIII[BI)V")
    public static native void glReadPixelsub(@OriginalArg(0) int x, @OriginalArg(1) int y, @OriginalArg(2) int width, @OriginalArg(3) int height, @OriginalArg(4) int format, @OriginalArg(5) int type, @OriginalArg(6) byte[] pixels, @OriginalArg(7) int pixelsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDeleteRenderbuffersEXT", descriptor = "(I[II)V")
    public static native void glDeleteRenderbuffersEXT(@OriginalArg(0) int n, @OriginalArg(1) int[] renderbuffers, @OriginalArg(2) int renderbuffersOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glLightf", descriptor = "(IIF)V")
    public static native void glLightf(@OriginalArg(0) int light, @OriginalArg(1) int pname, @OriginalArg(2) float param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glEnableClientState", descriptor = "(I)V")
    public static native void glEnableClientState(@OriginalArg(0) int array);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glHint", descriptor = "(II)V")
    public static native void glHint(@OriginalArg(0) int target, @OriginalArg(1) int mode);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glNormalPointer", descriptor = "(IIJ)V")
    public static native void glNormalPointer(@OriginalArg(0) int type, @OriginalArg(1) int stride, @OriginalArg(2) long pointer);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glAlphaFunc", descriptor = "(IF)V")
    public static native void glAlphaFunc(@OriginalArg(0) int func, @OriginalArg(1) float ref);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glProgramLocalParameter4fvARB", descriptor = "(II[FI)V")
    public static native void glProgramLocalParameter4fvARB(@OriginalArg(0) int target, @OriginalArg(1) int index, @OriginalArg(2) float[] params, @OriginalArg(3) int paramsOffset);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glVertex2f", descriptor = "(FF)V")
    public static native void glVertex2f(@OriginalArg(0) float x, @OriginalArg(1) float y);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glStencilOp", descriptor = "(III)V")
    public static native void glStencilOp(@OriginalArg(0) int fail, @OriginalArg(1) int zfail, @OriginalArg(2) int zpass);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glBufferSubDataARBa", descriptor = "(IIIJ)V")
    public static native void glBufferSubDataARBa(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) long arg3);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glDisableClientState", descriptor = "(I)V")
    public static native void glDisableClientState(@OriginalArg(0) int array);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "glFogi", descriptor = "(II)V")
    public static native void glFogi(@OriginalArg(0) int pname, @OriginalArg(1) int param);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "release", descriptor = "()V")
    public native void release();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "init", descriptor = "(Ljava/awt/Canvas;IIIIII)J")
    public native long init(@OriginalArg(0) Canvas arg0, @OriginalArg(1) int arg1, @OriginalArg(2) int arg2, @OriginalArg(3) int arg3, @OriginalArg(4) int arg4, @OriginalArg(5) int arg5, @OriginalArg(6) int arg6);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "surfaceResized", descriptor = "(J)V")
    public native void surfaceResized(@OriginalArg(0) long arg0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "a", descriptor = "(Ljava/lang/String;)Z")
    public boolean a(@OriginalArg(0) String arg0) {
        if (this.c == null) {
            this.c = new Hashtable();
            @Pc(10) String local10 = glGetString(7939);
            @Pc(12) int local12 = 0;
            while (true) {
                @Pc(17) int local17 = local10.indexOf(32, local12);
                if (local17 == -1) {
                    @Pc(47) String local47 = local10.substring(local12).trim();
                    if (local47.length() != 0) {
                        this.c.put(local47, local47);
                    }
                    break;
                }
                @Pc(26) String local26 = local10.substring(local12, local17).trim();
                if (local26.length() != 0) {
                    this.c.put(local26, local26);
                }
                local12 = local17 + 1;
            }
        }
        return this.c.containsKey(arg0);
    }

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "setPbuffer", descriptor = "(J)V")
    public native void setPbuffer(@OriginalArg(0) long arg0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "swapBuffers", descriptor = "()V")
    public native void swapBuffers();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "releaseSurface", descriptor = "(Ljava/awt/Canvas;J)V")
    public native void releaseSurface(@OriginalArg(0) Canvas arg0, @OriginalArg(1) long arg1);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "prepareSurface", descriptor = "(Ljava/awt/Canvas;)J")
    public native long prepareSurface(@OriginalArg(0) Canvas arg0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "createPbuffer", descriptor = "(II)J")
    public native long createPbuffer(@OriginalArg(0) int arg0, @OriginalArg(1) int arg1);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "setSurface", descriptor = "(J)Z")
    public native boolean setSurface(@OriginalArg(0) long arg0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "b", descriptor = "()Z")
    public synchronized boolean b() {
        @Pc(1) Thread local1 = Thread.currentThread();
        if (!this.attachPeer()) {
            return false;
        }
        @Pc(10) OpenGL local10 = (OpenGL) a.put(local1, this);
        if (local10 != null) {
            local10.b = null;
        }
        this.b = local1;
        return true;
    }

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "a", descriptor = "()Z")
    public synchronized boolean a() {
        if (this.b == Thread.currentThread()) {
            this.detachPeer();
            a.remove(this.b);
            this.b = null;
            return true;
        } else {
            return false;
        }
    }

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "releasePbuffer", descriptor = "(J)V")
    public native void releasePbuffer(@OriginalArg(0) long arg0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "detachPeer", descriptor = "()V")
    public native void detachPeer();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "attachPeer", descriptor = "()Z")
    public native boolean attachPeer();

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "setSwapInterval", descriptor = "(I)V")
    public native void setSwapInterval(@OriginalArg(0) int arg0);

    @OriginalMember(owner = "client!jaggl/OpenGL", name = "arePbuffersAvailable", descriptor = "()Z")
    public native boolean arePbuffersAvailable();
}
