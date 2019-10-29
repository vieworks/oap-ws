/*
 * The MIT License (MIT)
 *
 * Copyright (c) Open Application Platform Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package oap.ws.sso;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import oap.io.Resources;
import oap.ws.sso.testng.SSOTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntegratedTest extends SSOTest {
    public IntegratedTest() {
        super( Resources.filePath( SecurityInterceptorTest.class, "/application.test.conf" ).orElseThrow() );
    }

    protected TestUserStorage userStorage() {
        return kernelFixture.service( TestUserStorage.class );
    }

    @Slf4j
    public static class TestUserStorage implements UserStorage {
        public final List<User> users = new ArrayList<>();

        public void addUser( TestUser user ) {
            users.add( user );
        }

        @Override
        public Optional<User> getUser( String email ) {
            return users.stream().filter( u -> u.getEmail().equalsIgnoreCase( email ) ).findAny();
        }

        @Override
        public Optional<User> getAuthenticated( String email, String password ) {
            log.trace( "authenticating {} with {}", email, password );
            log.trace( "users {}", users );
            return users.stream()
                .filter( u -> u.getEmail().equalsIgnoreCase( email ) && u.getPassword().equals( password ) )
                .findAny();
        }
    }

    @ToString
    @EqualsAndHashCode
    public static class TestUser implements User {
        public final String email;
        public final String password;
        public final String role;

        public TestUser( String email, String password, String role ) {
            this.email = email;
            this.password = password;
            this.role = role;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getRole() {
            return role;
        }
    }
}