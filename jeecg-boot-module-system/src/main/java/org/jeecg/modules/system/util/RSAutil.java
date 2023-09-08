package org.jeecg.modules.system.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 功能描述
 *
 * @author: caiguapi
 * @date: 2022年09月08日 11:03
 */
public class RSAutil {

    /**
     * 加密戴发送信息的公钥
     */
    public static final String PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsGmH6+NS24pCVr/7sfgA\n" +
            "MR4pZUUjlf41Q9mM2rSpK8N8yTuvlMqeqxmzZPXFJI6T808gpAXNDtYd6t+Vy5iB\n" +
            "1DQG1tOSjYAODx1kCtIspePfNsCNhiqWQkD5tU2AZEayqScnSQ7XKZjppL1JMvI1\n" +
            "tDss0xzHPSDwuAxindiPgTW2LZRw4fxBe/6BOT9vnnEsLXDW2O/ZT/GiBExbBEF/\n" +
            "azr8c/NSqtH9uH5BlisUBzXUKLkx4wBnq7GNhVh8hEDpQgYls8rhYbLGF58U3QQO\n" +
            "5OCwvKRO+C9ZMpiltaEz3NDsDS/MFlPinCdcC2bBejpLQd/W0hRdj9H0SCnAPiDp\n" +
            "gwIDAQAB\n";

    /**
     * 解密前端信息的私钥
     */
    public static final String PrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDc8zwDCS45b9oq\n" +
            "3RmqcMqZS+koT10yVyvRbub1eSsj8+lf8ydJKqfEN7shFBnfdt7IKejSH3tteUOe\n" +
            "rbvc085gdcp6Kd2d4jFTAJKMY9YQxO8ba2oAIIS0WubcaGDjDnUsUmvgOPfd9Mbb\n" +
            "WBmE1O0vfgDlHMLZcALc3prGQVfsn7i7XygP9cmkJNvi+WHSfbs1HnZcsQywk7sy\n" +
            "tFkgs5LyHuc2SrOiKin7nOBGJFGMS/WvZDjjUG615ik7e9VD8jo8Mbs1Ph/J/JcO\n" +
            "ulvYglpCaGxD6wIlB2EDcJLo1mmwSJcq8MRaBwIhFv60hNdDCrsSV58dprg4kwj8\n" +
            "l/E1uowDAgMBAAECggEADZAAAN8JOi0DqHo+RJLm2hu+701WgawN0a3BOekwUjp7\n" +
            "JVDLemIcE98oj+0Sx3+dnjjLBKGPwIEJ5bgcCNVBh59DXgCq4gb2kyCSXlFp3ElS\n" +
            "X2MBCd/1IA2tD8jNGwg8qLX9ztuAWjICcN1gDPd1qw13fZjTMb3+lv3dD0qx3Hbg\n" +
            "5QkK4biEwGyhImZ3XJvVg5hGLqyCM+mKm+iHjUTSY2rVlrlEuZzU/SG8a+4LuDrS\n" +
            "pYRz1wvQhv5apIaulg9FPmBiYeI/zpfVguCh1qqcbG+VrnJ1Nv5Lq2iMxCyDXCwj\n" +
            "MV9k7mrp+GNvdEL+UlY9LGsyajicjLolUJA8SvYbDQKBgQDytvjwbAMJ+4bm6Let\n" +
            "kN8MLVHVlSJitgMhcCI/eJzc+4vL0Km0CdyifvB1lpyL3Xhtzn448yVj0vLrm3y7\n" +
            "gA4AiE9ojqyEKliafrYNAfcj/G95oyPmQlYWRm/oKZEGcIUv0A+Zz7vqvdr0w3dd\n" +
            "GdGFT4oHvaIMGibJzsLm8ZnP/QKBgQDpC0lic4F81Ua/6cof51lq/HFrQPQTtKTz\n" +
            "0eCLr2zzJTISOsdVC6FVFsmRz8vX6Gzp7oKpSUVcvE8aL9vGbZk99Pym6OxEe61F\n" +
            "WXtLf9TSyO+zzlJ7+ufZnyHlPH2IyyYtMLxi0VJf0Z5TP8u8Mefx+lAsg1MIy3hz\n" +
            "p8nvHM6L/wKBgBnrroRGlMCBKzQH0G3p1dhAEshDiAuQsp7kkdPDdsUm3BQlMap3\n" +
            "jyePdPCp6AgL2+umLNa6KNkTUzwUO+ruTxCBfjGFYykuOI4vUBPtjM48DE+qO5BA\n" +
            "r17roNHjdoiAhcjIaJIdnE0Cf5q6MUoxEq4xA2W2O9MdC91rhmcRkEKlAoGBANVe\n" +
            "hB4j61bEXzavc2SqiXAvGhk9KzwvlSe37YN3ZqVoVOPj9UWklF43su5R9y0DnVTC\n" +
            "D9e3Qk6aC0LKtsqC5l+Xwwt/D3PUWNy6BO0naVBksTNLoLHrFARocrxUGgv5FEcj\n" +
            "7Bf91xHr9q36Jytz9g3TRTy8MHZI9UvPQFAxa9s5AoGAdtLjHOsbAAOh3Vxf5f2u\n" +
            "S8d1MU/19UfTE2z7gMvkcGMjQqyu0ew/faIMRSf2RoFwfh3nF8OgdDibQaEEnFU1\n" +
            "44OItpzR0Tv7i2AAfwYf5MdRe8UV1K/0nd7kcMwhg+2o3fyETN3aAepGOhsbh+7e\n" +
            "s2mPFP22qx2yYIz13cglWso=\n";

    /**
     * RSA私钥解密
     * @param str  解密字符串
     * @param privateKey  私钥
     * @return 明文
     */
    public static String decrypt(String str, String privateKey) {
        //64位解码加密后的字符串
        byte[] inputByte;
        String outStr = "";
        try {
            inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return outStr;
    }

    /**
     *  RSA公钥加密
     * @param str 需要加密的字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

}
