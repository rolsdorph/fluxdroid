package io.rolsdorph.fluxdroid.data.sink;

public interface InfluxAuth {
    String getInfluxAuthHeader();

    final class UsernamePassword implements InfluxAuth {
        private final String authString;

        public UsernamePassword(String username, String password) {
            authString = "Token " + username + ":" + password;
        }

        @Override
        public String getInfluxAuthHeader() {
            return authString;
        }
    }

    final class Token implements InfluxAuth {
        private final String authHeader;

        public Token(String token) {
            this.authHeader = "Token " + token;
        }

        @Override
        public String getInfluxAuthHeader() {
            return authHeader;
        }
    }
}
