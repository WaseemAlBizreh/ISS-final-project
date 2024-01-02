package security;

import app.Utils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.jcajce.*;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class GenerateCSR {



    void createCSR(){
        // get KeyPair
        Utils utils = new Utils();

        KeyPair keyPair = utils.checkPgp();

        // Prepare CSR subject
        X500Name subject = new X500Name("CN=Your Common Name, O=Your Organization, C=Your Country");

        // Create CSR builder
        JcaPKCS10CertificationRequestBuilder csrBuilder = new JcaPKCS10CertificationRequestBuilder(subject, keyPair.getPublic());

        // Create ContentSigner
        ContentSigner contentSigner = null;
        try {
            contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());
        } catch (OperatorCreationException e) {
            throw new RuntimeException(e);
        }

        // Build the CSR
        PKCS10CertificationRequest csr = csrBuilder.build(contentSigner);

        // Convert CSR to PEM or DER format and save to file or use as needed
        String csrPem = null;
        try {
            csrPem = new String(csr.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("CSR in PEM format:");
        System.out.println(csrPem);

    }

    public static void main(String[] args) throws Exception {
        GenerateCSR generateCSR = new GenerateCSR();
        generateCSR.createCSR();

    }


}


