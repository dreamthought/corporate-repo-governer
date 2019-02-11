package com.largecorp.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Service
@Scope("singleton")
public class GitHubJWTProvider {
    private String key;
    private String jwt;
    private Instant expiry;
    private static final Integer TOKEN_DURATION_SECS = 10*60; // 10 Minutes
    Logger logger = LoggerFactory.getLogger(GitHubJWTProvider.class);

    @Value("${github.app.id}")
    private String githubAppId;

    @Value("${github.privatekey.location}")
    private String githubPrivateKeyLocation;

    /**
     * On instantiation this will read the private key and stash it
     * @throws IOException
     */
    @PostConstruct
    public void setPrivateKey() throws IOException {
        String pem = readPemFile( githubPrivateKeyLocation );
        // Java needs better base64 'file' support
        pem = pem.replace("-----BEGIN RSA PRIVATE KEY-----", "");
        key = pem.replace("-----END RSA PRIVATE KEY-----","");
    }

    /**
     * Lazily creates and returns a new JWT if one does not already exist.
     * @return
     */
    public String getJwt() {
        if (null == jwt || tokenHasExpired()) {
            jwt = rebuildJwt();
        }
        return jwt;
    }

    /**
     * Checks if the last token has expired
     * @return Booelan
     */
    private boolean tokenHasExpired() {
        return Instant.now().isAfter(expiry);
    }

    /**
     * Lazily reissue a new jwt
     * @return A new JWT with a future expiry
     */
    private String rebuildJwt() {
        Instant issuedAt = Instant.now();
        expiry = issuedAt.plusSeconds(TOKEN_DURATION_SECS);

        // FIXME: To save cycles and complete this exercise,
        // I've not bothered to Base64 decode
        String jwt = Jwts.builder().
            setIssuer(githubAppId).
            setIssuedAt(Date.from(issuedAt)).
            setExpiration(Date.from(expiry)).
            signWith(SignatureAlgorithm.ES256, key).compact();
        return jwt;
    }

    /**
     * Reads the pem file from the file system
     * @param githubPrivateKeyLocation
     * @return Contents of our Pem
     * @throws IOException
     */
    private String readPemFile(String githubPrivateKeyLocation) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(githubPrivateKeyLocation),
            StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder(1024);
        lines.forEach((line)->sb.append(line));
        return sb.toString();
    }

}
