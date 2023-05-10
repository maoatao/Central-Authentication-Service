package com.maoatao.cas.openapi;

import com.maoatao.cas.openapi.converter.JwtOperatorContextConverter;
import com.maoatao.daedalus.core.context.DaedalusOperatorContext;
import org.junit.Test;

/**
 * jwt测试
 *
 * @author MaoAtao
 * @date 2023-03-19 19:33:25
 */
public class JwtTokenTest {

    @Test
    public void read_jwt_token_test() {
        String token = "eyJjdXN0b21lckhlYWRlciI6Iui_meaYr-S4gOS4quiHquWumuS5iWFjY2Vzc190b2tlbiBoZWFkZXIiLCJhbGciOiJSUzI1NiIsImtpZCI6ImZhZGZiMWE5LWQ0ZDYtNDQxZi1hMGYwLTk3Zjc4YjNiMmRlNSJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoidGVzdC1jbGllbnQiLCJjdXN0b21lckNsYWltIjoi6L-Z5piv5LiA5Liq6Ieq5a6a5LmJYWNjZXNzX3Rva2VuIGNsYWltIiwibmJmIjoxNjc5NDk2NzgyLCJvcGVuSWQiOiJVNjQwMWUyOTQyYTBjYjI2MDIzYmEwNTVjIiwic2NvcGUiOlsidGVzdC53cml0ZSIsInRlc3QucmVhZCJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0IiwiZXhwIjoxNjc5NDk3MDgyLCJpYXQiOjE2Nzk0OTY3ODIsInVzZXJJZCI6MSwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlBFUk1JU1NJT05fVEVTVCIsImNsaWVudCI6InRlc3QtY2xpZW50In1dfQ.RG9aX0bqZCMCwNFL98ju7CTgo0n2bQafDslGC1l7Cs1iRf9WIvZXFyWQHO0SmZUKSN--LwkPR-rChxWG4A6FOGFBFqYCyIwgTYTW4-Z6IKj4T3q6Auh3htCeTPqMhvw5q14RIEk2D_PP66WkuNfg8X7g86KYv1cAbCEVWVhIH_jo2kzK294ZyXLqoE9Xf6D-fAHBjRdF1n5EdELS5xbqVr5032QuoTR01zprsKS2W7r38NZCpFARkQez73GbIUvScZtrcTmK0PhriINn4acOHKDuvS2SuLrd9J-OGUr4eCBG5Qo8vbgbn_y0OBt0U9z31yhXuiWDKxGlsmvOW_NiXw";
        DaedalusOperatorContext operatorContext = new JwtOperatorContextConverter("").convert(token);
        System.out.println(operatorContext);
    }
}
