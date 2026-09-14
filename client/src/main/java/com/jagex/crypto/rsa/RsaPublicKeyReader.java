package com.jagex.crypto.rsa;

import com.jagex.crypto.AlgorithmValidator;
import com.jagex.crypto.SubjectPublicKeyInfoReader;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers.rsaEncryption;

public final class RsaPublicKeyReader {

    public static RsaPublicKey read(Path path) throws IOException {
        try (var reader = new PemReader(Files.newBufferedReader(path))) {
            var spki = SubjectPublicKeyInfoReader.read(reader);
            var parameters = rsaParametersFrom(spki);

            var modulus = parameters.getModulus();
            var exponent = parameters.getExponent();

            return new RsaPublicKey(modulus, exponent);
        }
    }

    private static RSAKeyParameters rsaParametersFrom(SubjectPublicKeyInfo spki) throws IOException {
        validateRsa(spki);
        var publicKey = spki.parsePublicKey();
        var publicRsaKey = rsaPublicKeyFrom(publicKey);
        return parametersOf(publicRsaKey);
    }

    private static void validateRsa(SubjectPublicKeyInfo spki) throws IOException {
        validateRsa(spki.getAlgorithm());
    }

    private static void validateRsa(AlgorithmIdentifier id) throws IOException {
        AlgorithmValidator.validate(
            /* id = */ id,
            /* expectedAlgorithm = */ rsaEncryption,
            /* expectedParameters = */ DERNull.INSTANCE
        );
    }

    private static RSAPublicKey rsaPublicKeyFrom(ASN1Primitive publicKey) {
        return RSAPublicKey.getInstance(publicKey);
    }

    private static RSAKeyParameters parametersOf(RSAPublicKey key) {
        return new RSAKeyParameters(
            /* isPrivate = */ false,
            /* modulus = */ key.getModulus(),
            /* exponent = */ key.getPublicExponent()
        );
    }

    private RsaPublicKeyReader() {
        /* empty */
    }
}
