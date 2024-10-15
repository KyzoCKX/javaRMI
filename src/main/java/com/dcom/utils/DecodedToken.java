package com.dcom.utils;

import java.io.Serializable;

public class DecodedToken implements Serializable  {
    private String tokenString;
        private String userType;
        private long exp;
        private int userId;
        private long iat;

        public DecodedToken(String tokenString, String userType, long exp, int userId, long iat) {
            this.tokenString = tokenString;
            this.userType = userType;
            this.exp = exp;
            this.userId = userId;
            this.iat = iat;
        }

        public String getTokenString() {
            return tokenString;
        }

        public String getUserType() {
            return userType;
        }

        public long getExp() {
            return exp;
        }

        public int getUserId() {
            return userId;
        }

        public long getIat() {
            return iat;
        }

        @Override
        public String toString() {
            return "DecodedToken{" +
                    "tokenString='" + tokenString + '\'' +
                    "userType='" + userType + '\'' +
                    ", exp=" + exp +
                    ", userId=" + userId +
                    ", iat=" + iat +
                    '}';
        }
}
