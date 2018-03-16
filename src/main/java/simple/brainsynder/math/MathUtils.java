package simple.brainsynder.math;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import simple.brainsynder.utils.FaceUtil;

import java.text.DecimalFormat;
import java.util.Random;

public class MathUtils {
    public static final float nanoToSec = 1.0E-9F;
    public static final float FLOAT_ROUNDING_ERROR = 1.0E-6F;
    public static final float PI = 3.1415927F;
    public static final float PI2 = 6.2831855F;
    public static final float SQRT_3 = 1.7320508F;
    public static final float E = 2.7182817F;
    private static final int SIN_BITS = 14;
    private static final int SIN_MASK = 16383;
    private static final int SIN_COUNT = 16384;
    private static final float radFull = 6.2831855F;
    private static final float degFull = 360.0F;
    private static final float radToIndex = 2607.5945F;
    private static final float degToIndex = 45.511112F;
    public static final float radiansToDegrees = 57.295776F;
    public static final float radDeg = 57.295776F;
    public static final float degreesToRadians = 0.017453292F;
    public static final float degRad = 0.017453292F;
    private static final int ATAN2_BITS = 7;
    private static final int ATAN2_BITS2 = 14;
    private static final int ATAN2_MASK = 16383;
    private static final int ATAN2_COUNT = 16384;
    static final int ATAN2_DIM = (int)Math.sqrt(16384.0D);
    private static final float INV_ATAN2_DIM_MINUS_1;
    private static final int BIG_ENOUGH_INT = 16384;
    private static final double BIG_ENOUGH_FLOOR = 16384.0D;
    private static final double CEIL = 0.9999999D;
    private static final double BIG_ENOUGH_CEIL = 16384.999999999996D;
    private static final double BIG_ENOUGH_ROUND = 16384.5D;
    private static final int CHUNK_BITS = 4;
    private static final int CHUNK_VALUES = 16;
    public static final float DEGTORAD = 0.017453293F;
    public static final float RADTODEG = 57.29577951F;
    public static final double HALFROOTOFTWO = 0.707106781;
    private static Random random = new Random();

    public static double trim(int degree, double d) {
        StringBuilder format = new StringBuilder("#.#");

        for(int twoDForm = 1; twoDForm < degree; ++twoDForm) {
            format.append("#");
        }

        DecimalFormat var5 = new DecimalFormat(format.toString());
        return Double.valueOf(var5.format(d)).doubleValue();
    }
    /**
     * Gets the angle difference between two angles
     *
     * @param angle1
     * @param angle2
     * @return angle difference
     */
    public static int getAngleDifference(int angle1, int angle2) {
        return Math.abs(wrapAngle(angle1 - angle2));
    }
    /**
     * Wraps the angle to be between -180 and 180 degrees
     *
     * @param angle to wrap
     * @return [-180 > angle >= 180]
     */
    public static int wrapAngle(int angle) {
        int wrappedAngle = angle;
        while (wrappedAngle <= -180) {
            wrappedAngle += 360;
        }
        while (wrappedAngle > 180) {
            wrappedAngle -= 360;
        }
        return wrappedAngle;
    }

    /**
     * Wraps the angle to be between -180 and 180 degrees
     *
     * @param angle to wrap
     * @return [-180 > angle >= 180]
     */
    public static float wrapAngle(float angle) {
        float wrappedAngle = angle;
        while (wrappedAngle <= -180f) {
            wrappedAngle += 360f;
        }
        while (wrappedAngle > 180f) {
            wrappedAngle -= 360f;
        }
        return wrappedAngle;
    }

    /**
     * Normalizes a 2D-vector to be the length of another 2D-vector<br>
     * Calculates the normalization factor to multiply the input vector with, to get the requested length
     *
     * @param x axis of the vector
     * @param z axis of the vector
     * @param reqx axis of the length vector
     * @param reqz axis of the length vector
     * @return the normalization factor
     */
    public static double normalize(double x, double z, double reqx, double reqz) {
        return Math.sqrt(lengthSquared(reqx, reqz) / lengthSquared(x, z));
    }

    public static float getLookAtYaw(Entity loc, Entity lookat) {
        return getLookAtYaw(loc.getLocation(), lookat.getLocation());
    }

    public static float getLookAtYaw(Block loc, Block lookat) {
        return getLookAtYaw(loc.getLocation(), lookat.getLocation());
    }

    public static float getLookAtYaw(Location loc, Location lookat) {
        return getLookAtYaw(lookat.getX() - loc.getX(), lookat.getZ() - loc.getZ());
    }

    public static float getLookAtYaw(Vector motion) {
        return getLookAtYaw(motion.getX(), motion.getZ());
    }

    /**
     * Gets the horizontal look-at angle in degrees to look into the 2D-direction specified
     *
     * @param dx axis of the direction
     * @param dz axis of the direction
     * @return the angle in degrees
     */
    public static float getLookAtYaw(double dx, double dz) {
        return atan2(dz, dx) - 180f;
    }

    public static double lengthSquared(double... values) {
        double rval = 0;
        for (double value : values) {
            rval += value * value;
        }
        return rval;
    }

    public static double length(double... values) {
        return Math.sqrt(lengthSquared(values));
    }

    /**
     * Gets the pitch angle in degrees to look into the direction specified
     *
     * @param dX axis of the direction
     * @param dY axis of the direction
     * @param dZ axis of the direction
     * @return look-at angle in degrees
     */
    public static float getLookAtPitch(double dX, double dY, double dZ) {
        return getLookAtPitch(dY, length(dX, dZ));
    }

    /**
     * Gets the pitch angle in degrees to look into the direction specified
     *
     * @param dY axis of the direction
     * @param dXZ axis of the direction (length of x and z)
     * @return look-at angle in degrees
     */
    public static float getLookAtPitch(double dY, double dXZ) {
        return -atan(dY / dXZ);
    }

    /**
     * Gets the inverse tangent of the value in degrees
     *
     * @param value
     * @return inverse tangent angle in degrees
     */
    public static float atan(double value) {
        return RADTODEG * (float) TrigMath.atan(value);
    }

    /**
     * Gets the inverse tangent angle in degrees of the rectangle vector
     *
     * @param y axis
     * @param x axis
     * @return inverse tangent 2 angle in degrees
     */
    public static float atan2(double y, double x) {
        return RADTODEG * (float) TrigMath.atan2(y, x);
    }

    /**
     * Gets the floor integer value from a double value
     *
     * @param value to get the floor of
     * @return floor value
     */
    public static int floor(double value) {
        int i = (int) value;
        return value < (double) i ? i - 1 : i;
    }

    /**
     * Gets the ceiling integer value from a double value
     *
     * @param value to get the ceiling of
     * @return ceiling value
     */
    public static int ceil(double value) {
        return -floor(-value);
    }

    /**
     * Moves a Location into the yaw and pitch of the Location in the offset specified
     *
     * @param loc to move
     * @param offset vector
     * @return Translated Location
     */
    public static Location move(Location loc, Vector offset) {
        return move(loc, offset.getX(), offset.getY(), offset.getZ());
    }

    /**
     * Moves a Location into the yaw and pitch of the Location in the offset specified
     *
     * @param loc to move
     * @param dx offset
     * @param dy offset
     * @param dz offset
     * @return Translated Location
     */
    public static Location move(Location loc, double dx, double dy, double dz) {
        Vector off = rotate(loc.getYaw(), loc.getPitch(), dx, dy, dz);
        double x = loc.getX() + off.getX();
        double y = loc.getY() + off.getY();
        double z = loc.getZ() + off.getZ();
        return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
    }

    /**
     * Rotates a 3D-vector using yaw and pitch
     *
     * @param yaw angle in degrees
     * @param pitch angle in degrees
     * @param vector to rotate
     * @return Vector rotated by the angle (new instance)
     */
    public static Vector rotate(float yaw, float pitch, Vector vector) {
        return rotate(yaw, pitch, vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Rotates a 3D-vector using yaw and pitch
     *
     * @param yaw angle in degrees
     * @param pitch angle in degrees
     * @param x axis of the vector
     * @param y axis of the vector
     * @param z axis of the vector
     * @return Vector rotated by the angle
     */
    public static Vector rotate(float yaw, float pitch, double x, double y, double z) {
        // Conversions found by (a lot of) testing
        float angle;
        angle = yaw * DEGTORAD;
        double sinyaw = Math.sin(angle);
        double cosyaw = Math.cos(angle);

        angle = pitch * DEGTORAD;
        double sinpitch = Math.sin(angle);
        double cospitch = Math.cos(angle);

        Vector vector = new Vector();
        vector.setX((x * sinyaw) - (y * cosyaw * sinpitch) - (z * cosyaw * cospitch));
        vector.setY((y * cospitch) - (z * sinpitch));
        vector.setZ(-(x * cosyaw) - (y * sinyaw * sinpitch) - (z * sinyaw * cospitch));
        return vector;
    }

    /**
     * Rounds the specified value to the amount of decimals specified
     *
     * @param value to round
     * @param decimals count
     * @return value round to the decimal count specified
     */
    public static double round(double value, int decimals) {
        double p = Math.pow(10, decimals);
        return Math.round(value * p) / p;
    }

    /**
     * Returns 0 if the value is not-a-number
     *
     * @param value to check
     * @return The value, or 0 if it is NaN
     */
    public static double fixNaN(double value) {
        return fixNaN(value, 0.0);
    }

    /**
     * Returns the default if the value is not-a-number
     *
     * @param value to check
     * @param def value
     * @return The value, or the default if it is NaN
     */
    public static double fixNaN(double value, double def) {
        return Double.isNaN(value) ? def : value;
    }

    /**
     * Converts a location value into a chunk coordinate
     *
     * @param loc to convert
     * @return chunk coordinate
     */
    public static int toChunk(double loc) {
        return floor(loc / (double) CHUNK_VALUES);
    }

    /**
     * Converts a location value into a chunk coordinate
     *
     * @param loc to convert
     * @return chunk coordinate
     */
    public static int toChunk(int loc) {
        return loc >> CHUNK_BITS;
    }

    public static double useOld(double oldvalue, double newvalue, double peruseold) {
        return oldvalue + (peruseold * (newvalue - oldvalue));
    }

    public static double lerp(double d1, double d2, double stage) {
        if (Double.isNaN(stage) || stage > 1) {
            return d2;
        } else if (stage < 0) {
            return d1;
        } else {
            return d1 * (1 - stage) + d2 * stage;
        }
    }

    public static Vector lerp(Vector vec1, Vector vec2, double stage) {
        Vector newvec = new Vector();
        newvec.setX(lerp(vec1.getX(), vec2.getX(), stage));
        newvec.setY(lerp(vec1.getY(), vec2.getY(), stage));
        newvec.setZ(lerp(vec1.getZ(), vec2.getZ(), stage));
        return newvec;
    }

    public static Location lerp(Location loc1, Location loc2, double stage) {
        Location newloc = new Location(loc1.getWorld(), 0, 0, 0);
        newloc.setX(lerp(loc1.getX(), loc2.getX(), stage));
        newloc.setY(lerp(loc1.getY(), loc2.getY(), stage));
        newloc.setZ(lerp(loc1.getZ(), loc2.getZ(), stage));
        newloc.setYaw((float) lerp(loc1.getYaw(), loc2.getYaw(), stage));
        newloc.setPitch((float) lerp(loc1.getPitch(), loc2.getPitch(), stage));
        return newloc;
    }

    /**
     * Checks whether one value is negative and the other positive, or opposite
     *
     * @param value1 to check
     * @param value2 to check
     * @return True if value1 is inverted from value2
     */
    public static boolean isInverted(double value1, double value2) {
        return (value1 > 0 && value2 < 0) || (value1 < 0 && value2 > 0);
    }

    /**
     * Gets the direction of yaw and pitch angles
     *
     * @param yaw angle in degrees
     * @param pitch angle in degrees
     * @return Direction Vector
     */
    public static Vector getDirection(float yaw, float pitch) {
        Vector vector = new Vector();
        double rotX = DEGTORAD * yaw;
        double rotY = DEGTORAD * pitch;
        vector.setY(-Math.sin(rotY));
        double h = Math.cos(rotY);
        vector.setX(-h * Math.sin(rotX));
        vector.setZ(h * Math.cos(rotX));
        return vector;
    }

    /**
     * Clamps the value between -limit and limit
     *
     * @param value to clamp
     * @param limit
     * @return value, -limit or limit
     */
    public static double clamp(double value, double limit) {
        return clamp(value, -limit, limit);
    }

    /**
     * Clamps the value between the min and max values
     * @param value to clamp
     * @param min
     * @param max
     * @return value, min or max
     */
    public static double clamp(double value, double min, double max) {
        return value < min ? min : (value > max ? max : value);
    }

    /**
     * Clamps the value between -limit and limit
     *
     * @param value to clamp
     * @param limit
     * @return value, -limit or limit
     */
    public static float clamp(float value, float limit) {
        return clamp(value, -limit, limit);
    }

    /**
     * Clamps the value between -limit and limit
     *
     * @param value to clamp
     * @param limit
     * @return value, -limit or limit
     */
    public static int clamp(int value, int limit) {
        return clamp(value, -limit, limit);
    }

    /**
     * Turns a value negative or keeps it positive based on a boolean input
     *
     * @param value to work with
     * @param negative - True to invert, False to keep the old value
     * @return the value or inverted (-value)
     */
    public static int invert(int value, boolean negative) {
        return negative ? -value : value;
    }

    /**
     * Turns a value negative or keeps it positive based on a boolean input
     *
     * @param value to work with
     * @param negative - True to invert, False to keep the old value
     * @return the value or inverted (-value)
     */
    public static float invert(float value, boolean negative) {
        return negative ? -value : value;
    }

    /**
     * Turns a value negative or keeps it positive based on a boolean input
     *
     * @param value to work with
     * @param negative - True to invert, False to keep the old value
     * @return the value or inverted (-value)
     */
    public static double invert(double value, boolean negative) {
        return negative ? -value : value;
    }

    public static void setVectorLength(Vector vector, double length) {
        setVectorLengthSquared(vector, Math.signum(length) * length * length);
    }

    public static void setVectorLengthSquared(Vector vector, double lengthsquared) {
        double vlength = vector.lengthSquared();
        if (Math.abs(vlength) > 0.0001) {
            if (lengthsquared < 0) {
                vector.multiply(-Math.sqrt(-lengthsquared / vlength));
            } else {
                vector.multiply(Math.sqrt(lengthsquared / vlength));
            }
        }
    }

    public static boolean isHeadingTo(BlockFace direction, Vector velocity) {
        return isHeadingTo(FaceUtil.faceToVector(direction), velocity);
    }

    public static boolean isHeadingTo(Location from, Location to, Vector velocity) {
        return isHeadingTo(new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ()), velocity);
    }

    public static boolean isHeadingTo(Vector offset, Vector velocity) {
        double dbefore = offset.lengthSquared();
        if (dbefore < 0.0001) {
            return true;
        }
        Vector clonedVelocity = velocity.clone();
        setVectorLengthSquared(clonedVelocity, dbefore);
        return dbefore > clonedVelocity.subtract(offset).lengthSquared();
    }

    /**
     * Gets the angle difference between two angles
     *
     * @param angle1
     * @param angle2
     * @return angle difference
     */
    public static float getAngleDifference(float angle1, float angle2) {
        return Math.abs(wrapAngle(angle1 - angle2));
    }

    public static int r(int i) {
        return random.nextInt(i);
    }

    public static double offset2d(Entity a, Entity b) {
        return offset2d(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset2d(Location a, Location b) {
        return offset2d(a.toVector(), b.toVector());
    }

    public static double offset2d(Vector a, Vector b) {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    public static float sin(float radians) {
        return MathUtils.Sin.table[(int)(radians * 2607.5945F) & 16383];
    }

    public static float cos(float radians) {
        return MathUtils.Sin.table[(int)((radians + 1.5707964F) * 2607.5945F) & 16383];
    }

    public static float sinDeg(float degrees) {
        return MathUtils.Sin.table[(int)(degrees * 45.511112F) & 16383];
    }

    public static float cosDeg(float degrees) {
        return MathUtils.Sin.table[(int)((degrees + 90.0F) * 45.511112F) & 16383];
    }

    public static boolean isInteger(Object object) {
        try {
            Integer.parseInt(object.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isDouble(Object object) {
        try {
            Double.parseDouble(object.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static float atan2(float y, float x) {
        float add;
        float mul;
        if(x < 0.0F) {
            if(y < 0.0F) {
                y = -y;
                mul = 1.0F;
            } else {
                mul = -1.0F;
            }

            x = -x;
            add = -3.1415927F;
        } else {
            if(y < 0.0F) {
                y = -y;
                mul = -1.0F;
            } else {
                mul = 1.0F;
            }

            add = 0.0F;
        }

        float invDiv = 1.0F / ((x < y?y:x) * INV_ATAN2_DIM_MINUS_1);
        if(invDiv == 1.0F / 0.0) {
            return ((float)Math.atan2((double)y, (double)x) + add) * mul;
        } else {
            int xi = (int)(x * invDiv);
            int yi = (int)(y * invDiv);
            return (MathUtils.Atan2.table[yi * ATAN2_DIM + xi] + add) * mul;
        }
    }

    public static int random(int range) {
        return random.nextInt(range + 1);
    }

    public static int random(int start, int end) {
        return start + random.nextInt(end - start + 1);
    }

    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static boolean randomBoolean(float chance) {
        return random() < chance;
    }

    public static float random() {
        return random.nextFloat();
    }

    public static float random(float range) {
        return random.nextFloat() * range;
    }

    public static float random(float start, float end) {
        return start + random.nextFloat() * (end - start);
    }

    public static int nextPowerOfTwo(int value) {
        if(value == 0) {
            return 1;
        } else {
            --value;
            value |= value >> 1;
            value |= value >> 2;
            value |= value >> 4;
            value |= value >> 8;
            value |= value >> 16;
            return value + 1;
        }
    }

    public static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    public static int clamp(int value, int min, int max) {
        return value < min?min:(value > max?max:value);
    }

    public static short clamp(short value, short min, short max) {
        return value < min?min:(value > max?max:value);
    }

    public static float clamp(float value, float min, float max) {
        return value < min?min:(value > max?max:value);
    }

    public static int floor(float x) {
        return (int)((double)x + 16384.0D) - 16384;
    }

    public static int floorPositive(float x) {
        return (int)x;
    }

    public static int ceil(float x) {
        return (int)((double)x + 16384.999999999996D) - 16384;
    }

    public static int ceilPositive(float x) {
        return (int)((double)x + 0.9999999D);
    }

    public static int round(float x) {
        return (int)((double)x + 16384.5D) - 16384;
    }

    public static int roundPositive(float x) {
        return (int)(x + 0.5F);
    }

    public static boolean isZero(float value) {
        return Math.abs(value) <= 1.0E-6F;
    }

    public static boolean isZero(float value, float tolerance) {
        return Math.abs(value) <= tolerance;
    }

    public static boolean isEqual(float a, float b) {
        return Math.abs(a - b) <= 1.0E-6F;
    }

    public static boolean isEqual(float a, float b, float tolerance) {
        return Math.abs(a - b) <= tolerance;
    }

    public static Vector rotateAroundAxisX(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    public static Vector rotateAroundAxisY(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    public static Vector rotateAroundAxisZ(Vector v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    public static Vector rotateVector(Vector v, double angleX, double angleY, double angleZ) {
        rotateAroundAxisX(v, angleX);
        rotateAroundAxisY(v, angleY);
        rotateAroundAxisZ(v, angleZ);
        return v;
    }

    public static double angleToXAxis(Vector vector) {
        return Math.atan2(vector.getX(), vector.getY());
    }

    public static Vector getRandomVector() {
        double x = random.nextDouble() * 2.0D - 1.0D;
        double y = random.nextDouble() * 2.0D - 1.0D;
        double z = random.nextDouble() * 2.0D - 1.0D;
        return (new Vector(x, y, z)).normalize();
    }

    public static void applyVelocity(final Entity ent, Vector v) {
        if(!ent.hasMetadata("NPC")) {
            ent.setVelocity(v);
        }
    }

    public static Vector getRandomCircleVector() {
        double rnd = random.nextDouble() * 2.0D * 3.141592653589793D;
        double x = Math.cos(rnd);
        double z = Math.sin(rnd);
        return new Vector(x, 0.0D, z);
    }

    public static Material getRandomMaterial(Material[] materials) {
        return materials[random.nextInt(materials.length)];
    }

    public static double getRandomAngle() {
        return random.nextDouble() * 2.0D * 3.141592653589793D;
    }

    public static double randomDouble(double min, double max) {
        return Math.random() < 0.5D?(1.0D - Math.random()) * (max - min) + min:Math.random() * (max - min) + min;
    }

    public static float randomRangeFloat(float min, float max) {
        return (float)(Math.random() < 0.5D?(1.0D - Math.random()) * (double)(max - min) + (double)min:Math.random() * (double)(max - min) + (double)min);
    }

    public static byte randomByte(int max) {
        return (byte)random.nextInt(max + 1);
    }

    public static int randomRangeInt(int min, int max) {
        return (int)(Math.random() < 0.5D?(1.0D - Math.random()) * (double)(max - min) + (double)min:Math.random() * (double)(max - min) + (double)min);
    }

    public static double offset(Entity a, Entity b) {
        return offset(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset(Location a, Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    static {
        INV_ATAN2_DIM_MINUS_1 = 1.0F / (float)(ATAN2_DIM - 1);
        random = new Random();
    }

    private static class Atan2 {
        static final float[] table = new float[16384];

        private Atan2() {
        }

        static {
            for(int i = 0; i < MathUtils.ATAN2_DIM; ++i) {
                for(int j = 0; j < MathUtils.ATAN2_DIM; ++j) {
                    float x0 = (float)i / (float) MathUtils.ATAN2_DIM;
                    float y0 = (float)j / (float) MathUtils.ATAN2_DIM;
                    table[j * MathUtils.ATAN2_DIM + i] = (float)Math.atan2((double)y0, (double)x0);
                }
            }

        }
    }

    private static class Sin {
        static final float[] table = new float[16384];

        private Sin() {
        }

        static {
            int i;
            for(i = 0; i < 16384; ++i) {
                table[i] = (float)Math.sin((double)(((float)i + 0.5F) / 16384.0F * 6.2831855F));
            }

            for(i = 0; i < 360; i += 90) {
                table[(int)((float)i * 45.511112F) & 16383] = (float)Math.sin((double)((float)i * 0.017453292F));
            }

        }
    }
}
