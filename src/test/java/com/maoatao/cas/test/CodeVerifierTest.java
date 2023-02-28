package com.maoatao.cas.test;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author MaoAtao
 * @date 2022-10-15 19:42:36
 */
public class CodeVerifierTest {

    private final StringKeyGenerator secureKeyGenerator =
        new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

    @Test
    void contextLoads() throws NoSuchAlgorithmException {
        String codeVerifier = generateCodeVerifier();
        String codeChallange = generateCodeChallange(codeVerifier);
        System.out.println(codeVerifier);
        System.out.println(codeChallange);
    }


    /**
     *  <p>
     *      code_verifier
     *      对于每一个OAuth 授权请求， 客户端会先创建一个代码验证器 code_verifier，
     *      这是一个高熵加密的随机字符串， 使用URI 非保留字符 (Unreserved characters)，
     *      范围 [A-Z] / [a-z] / [0-9] / "-" / "." / "_" / "~"，
     *      因为非保留字符在传递时不需要进行 URL 编码， 并且 code_verifier 的长度最小是 43， 最大是 128，
     *      code_verifier 要具有足够的熵它是难以猜测的.
     *      code_verifier 的扩充巴科斯范式 (ABNF) 如下:
     *          code-verifier = 43*128unreserved
     *          unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
     *          ALPHA = %x41-5A / %x61-7A
     *          DIGIT = %x30-39
     *      简单点说就是在 [A-Z] / [a-z] / [0-9] / "-" / "." / "_" / "~" 范围内，生成43-128位的随机字符串。
     *  </p>
     *
     *  <p>
     *      Required: Node.js crypto module
     *      https://nodejs.org/api/crypto.html#crypto_crypto
     *      function base64URLEncode(str) {
     *          return str.toString('base64')
     *              .replace(/\+/g， '-')
     *              .replace(/\//g， '_')
     *              .replace(/=/g， '');
     *      }
     *      var verifier = base64URLEncode(crypto.randomBytes(32));
     *  </p>
     */
    String generateCodeVerifier() {
        return secureKeyGenerator.generateKey();
    }

    /**
     *  <p>
     *      code_challenge_method
     *      对 code_verifier 进行转换的方法， 这个参数会传给授权服务器， 并且授权服务器会记住这个参数， 颁发令牌的时候进行对比
     *      code_challenge == code_challenge_method(code_verifier) ， 若一致则颁发令牌。
     *      code_challenge_method 可以设置为 plain (原始值) 或者 S256 (sha256哈希)。
     *  </p>
     *  <p>
     *      code_challenge
     *      使用 code_challenge_method 对 code_verifier 进行转换得到 code_challenge，
     *      可以使用下面的方式进行转换
     *      plain   code_challenge = code_verifier
     *      S256    code_challenge = BASE64URL-ENCODE(SHA256(ASCII(code_verifier)))
     *      客户端应该首先考虑使用 S256 进行转换， 如果不支持，才使用 plain ， 此时 code_challenge 和 code_verifier 的值相等。
     *  </p>
     *  <p>
     *      Required: Node.js crypto module
     *      https://nodejs.org/api/crypto.html#crypto_crypto
     *      function sha256(buffer) {
     *          return crypto.createHash('sha256').update(buffer).digest();
     *      }
     *      var challenge = base64URLEncode(sha256(verifier));
     *  </p>
     */
    String generateCodeChallange(String codeVerifier) throws NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
