package cn.onecolour.utils;

/**
 * @author yang
 * @date 2022/7/12
 * @description
 */
@SuppressWarnings("unused")
public class HexUtils {

    private static final char[] HexCharArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String HexStr = "0123456789abcdef";

    public static String byteArrayToHexString(byte[] btArr) {
        char[] strArr = new char[btArr.length * 2];
        int i = 0;
        for (byte bt : btArr) {
            strArr[i++] = HexCharArr[bt >>> 4 & 0xf];
            strArr[i++] = HexCharArr[bt & 0xf];
        }
        return new String(strArr);
    }

    public static byte[] hexStringToByteArray(String hexString) {
        char[] charArr = hexString.toCharArray();
        byte[] btArr = new byte[charArr.length / 2];
        int index = 0;
        for (int i = 0; i < charArr.length; i++) {
            int highBit = HexStr.indexOf(charArr[i]);
            int lowBit = HexStr.indexOf(charArr[++i]);
            btArr[index] = (byte) (highBit << 4 | lowBit);
            index++;
        }
        return btArr;
    }
}
