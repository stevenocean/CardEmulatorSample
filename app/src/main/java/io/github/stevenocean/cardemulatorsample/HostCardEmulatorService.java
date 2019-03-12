package io.github.stevenocean.cardemulatorsample;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.widget.Toast;


public class HostCardEmulatorService extends HostApduService {

    public static final String TAG = HostCardEmulatorService.class.getName();

    // self-defined APDU
    public static final String STATUS_SUCCESS = "9000";
    public static final String STATUS_FAILED = "6F00";
    public static final String CLA_NOT_SUPPORTED = "6E00";
    public static final String INS_NOT_SUPPORTED = "6D00";
    public static final String AID = "A0000002471002";
    public static final String SELECT_INS = "A4";
    public static final String DEFAULT_CLA = "00";
    public static final int MIN_APDU_LENGTH = 12;

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {

        Toast.makeText(this, "processCommandApdu", Toast.LENGTH_LONG).show();

        if (commandApdu == null) {
            return hexStringToByteArray(STATUS_SUCCESS);
        }

        final String hexCommandApdu = encodeHexString(commandApdu);
        if (hexCommandApdu.length() < MIN_APDU_LENGTH) {
            return hexStringToByteArray(STATUS_FAILED);
        }

        if (!hexCommandApdu.substring(0, 2).equals(DEFAULT_CLA)) {
            return hexStringToByteArray(CLA_NOT_SUPPORTED);
        }

        if (!hexCommandApdu.substring(2, 4).equals(SELECT_INS)) {
            return hexStringToByteArray(INS_NOT_SUPPORTED);
        }

        if (hexCommandApdu.substring(10, 24).equals(AID))  {
            return hexStringToByteArray(STATUS_SUCCESS);
        } else {
            return hexStringToByteArray(STATUS_FAILED);
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Toast.makeText(this, "Deactivated - reason:" + reason, Toast.LENGTH_LONG).show();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public static String encodeHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte aByteArray : byteArray) {
            hexStringBuffer.append(byteToHex(aByteArray));
        }
        return hexStringBuffer.toString();
    }

}
