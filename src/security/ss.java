package security;

import lw.bouncycastle.bcpg.ArmoredInputStream;
import lw.bouncycastle.bcpg.ArmoredOutputStream;
import lw.bouncycastle.openpgp.*;
import lw.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import lw.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import lw.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import lw.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import java.io.*;
import java.security.*;
import java.util.Date;
import java.util.Iterator;

public class  ss {


    private static PGPKeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException, PGPException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PGPKeyPair pgpKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keyPair, new Date());

        return pgpKeyPair;
    }


    private static byte[] encryptMessage(String message, PGPPublicKey publicKey) throws IOException, PGPException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ArmoredOutputStream armoredOutputStream = new ArmoredOutputStream(byteArrayOutputStream);

        PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5).setWithIntegrityPacket(true).setSecureRandom(new SecureRandom()).setProvider("BC")
        );
        encryptedDataGenerator.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(publicKey).setProvider("BC"));

        OutputStream encryptedOut = encryptedDataGenerator.open(armoredOutputStream, new byte[4096]);

        encryptedOut.write(message.getBytes());

        encryptedOut.close();

        armoredOutputStream.close();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

//    private static String decryptMessage(byte[] encryptedData, PGPPrivateKey privateKey) throws IOException, PGPException {
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(encryptedData);
//        ArmoredInputStream armoredInputStream = new ArmoredInputStream(byteArrayInputStream);
//
//        PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(armoredInputStream, new JcaKeyFingerprintCalculator());
//
//        Object object = pgpObjectFactory.nextObject();
//        if (object instanceof PGPEncryptedDataList) {
//            PGPEncryptedDataList encryptedDataList = (PGPEncryptedDataList) object;
//
//            // البحث عن المفتاح المناسب لفك تشفير البيانات
//            Iterator<PGPEncryptedData> encryptedDataIterator = encryptedDataList.getEncryptedDataObjects();
//            PGPPrivateKeyDataDecryptorFactory dataDecryptorFactory = new JcePGPPrivateKeyDataDecryptorFactoryBuilder().setProvider("BC").build(privateKey);
//            PGPPublicKeyEncryptedData encryptedDataItem = null;
//            while (encryptedDataIterator.hasNext()) {
//                encryptedDataItem = (PGPPublicKeyEncryptedData) encryptedDataIterator.next();
//                if (encryptedDataItem.getKeyID() == privateKey.getKeyID()) {
//                    break;
//                }
//            }
//
//            if (encryptedDataItem != null) {
//                InputStream decryptedInputStream = encryptedDataItem.getDataStream(dataDecryptorFactory);
//                ByteArrayOutputStream decryptedOutputStream = new ByteArrayOutputStream();
//
//                byte[] buffer= new byte[4096];
//                int bytesRead;
//                while ((bytesRead = decryptedInputStream.read(buffer)) != -1) {
//                    decryptedOutputStream.write(buffer, 0, bytesRead);
//                }
//
//                decryptedInputStream.close();
//                decryptedOutputStream.close();
//
//                return decryptedOutputStream.toString();
//            }
//        }
//
//        return null;
//    }
}