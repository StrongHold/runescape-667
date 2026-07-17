package com.jagex.core.io.socket;

import com.jagex.core.constants.Ports;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

@OriginalClass("client!ob")
public final class ProxySocketFactory extends SocketFactory {

    @OriginalMember(owner = "client!ob", name = "h", descriptor = "Ljava/net/ProxySelector;")
    private final ProxySelector selector = ProxySelector.getDefault();

    @OriginalMember(owner = "client!ob", name = "i", descriptor = "Ljava/lang/Class;")
    private static Class urlClass;

    @OriginalMember(owner = "client!ob", name = "j", descriptor = "Ljava/lang/Class;")
    private static Class stringClass;

    @OriginalMember(owner = "client!ob", name = "<init>", descriptor = "()V")
    public ProxySocketFactory() {
    }

    @OriginalMember(owner = "client!ob", name = "a", descriptor = "(BLjava/net/Proxy;)Ljava/net/Socket;")
    public Socket proxyConnect(@OriginalArg(1) Proxy proxy) throws IOException {
        if (proxy.type() == Type.DIRECT) {
            return this.create();
        }

        @Pc(11) SocketAddress proxyAddress = proxy.address();
        if (!(proxyAddress instanceof InetSocketAddress)) {
            return null;
        }

        @Pc(28) InetSocketAddress address = (InetSocketAddress) proxyAddress;
        if (proxy.type() == Type.HTTP) {
            @Pc(34) String header = null;
            try {
                @Pc(39) Class authInfo = Class.forName("sun.net.www.protocol.http.AuthenticationInfo");
                @Pc(65) Method getProxyAuth = authInfo.getDeclaredMethod("getProxyAuth", stringClass == null ? (stringClass = Class.forName("java.lang.String")) : stringClass, Integer.TYPE);
                getProxyAuth.setAccessible(true);

                @Pc(87) Object object = getProxyAuth.invoke(null, address.getHostName(), Integer.valueOf(address.getPort()));
                if (object != null) {
                    @Pc(98) Method supportsPreemptiveAuthorization = authInfo.getDeclaredMethod("supportsPreemptiveAuthorization");
                    supportsPreemptiveAuthorization.setAccessible(true);
                    if ((Boolean) supportsPreemptiveAuthorization.invoke(object)) {
                        @Pc(119) Method getHeaderName = authInfo.getDeclaredMethod("getHeaderName");
                        getHeaderName.setAccessible(true);

                        @Pc(158) Method getHeaderValue = authInfo.getDeclaredMethod("getHeaderValue", urlClass == null ? (urlClass = Class.forName("java.net.URL")) : urlClass, stringClass == null ? (stringClass = Class.forName("java.lang.String")) : stringClass);
                        getHeaderValue.setAccessible(true);

                        @Pc(168) String headerName = (String) getHeaderName.invoke(object);
                        @Pc(200) String headerValue = (String) getHeaderValue.invoke(object, new URL("https://" + this.host + "/"), "https");
                        header = headerName + ": " + headerValue;
                    }
                } else {
                    System.out.println("No auth info!");
                }
            } catch (@Pc(215) Exception ignored) {
                /* empty */
            }

            System.out.println("Using auth: " + header);
            return this.httpProxyConnect(address.getHostName(), address.getPort(), header);
        } else if (proxy.type() == Type.SOCKS) {
            @Pc(232) Socket socket = new Socket(proxy);
            socket.connect(new InetSocketAddress(this.host, this.port));
            return socket;
        } else {
            return null;
        }
    }

    @OriginalMember(owner = "client!ob", name = "httpProxyConnect", descriptor = "(Ljava/lang/String;ILjava/lang/String;)Ljava/net/Socket;")
    public Socket httpProxyConnect(@OriginalArg(0) String arg0, @OriginalArg(1) int arg1, @OriginalArg(2) String arg2) throws IOException {
        @Pc(5) Socket socket = new Socket(arg0, arg1);
        socket.setSoTimeout(10000);

        @Pc(11) OutputStream out = socket.getOutputStream();
        if (arg2 == null) {
            out.write(("CONNECT " + this.host + ":" + this.port + " HTTP/1.0\n\n").getBytes(Charset.forName("ISO-8859-1")));
        } else {
            out.write(("CONNECT " + this.host + ":" + this.port + " HTTP/1.0\n" + arg2 + "\n\n").getBytes(Charset.forName("ISO-8859-1")));
        }
        out.flush();

        @Pc(86) BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        @Pc(89) String line = reader.readLine();
        if (line != null) {
            if (line.startsWith("HTTP/1.0 200") || line.startsWith("HTTP/1.1 200")) {
                return socket;
            }

            if (line.startsWith("HTTP/1.0 407") || line.startsWith("HTTP/1.1 407")) {
                @Pc(129) int lines = 0;
                @Pc(133) String challenge = "proxy-authenticate: ";

                System.out.println("Authenticating Proxy!");

                for (line = reader.readLine(); line != null && lines < 50; line = reader.readLine()) {
                    System.out.println(line);

                    if (line.toLowerCase().startsWith(challenge)) {
                        line = line.substring(challenge.length()).trim();

                        @Pc(164) int indexOfSpace = line.indexOf(' ');
                        if (indexOfSpace != -1) {
                            line = line.substring(0, indexOfSpace);
                        }

                        throw new ProxyAuthenticationException(line);
                    }

                    lines++;
                }

                throw new ProxyAuthenticationException("");
            }

            System.out.println("Got line from proxy: " + line);
        }

        out.close();
        reader.close();
        socket.close();
        return null;
    }

    @OriginalMember(owner = "client!ob", name = "b", descriptor = "(I)Ljava/net/Socket;")
    @Override
    public Socket open() throws IOException {
        @Pc(5) boolean useSystemProxies = Boolean.parseBoolean(System.getProperty("java.net.useSystemProxies"));
        if (!useSystemProxies) {
            System.setProperty("java.net.useSystemProxies", "true");
        }

        @Pc(26) boolean secure = this.port == Ports.HTTPS;

        @Pc(55) List primary;
        @Pc(84) List secondary;
        try {
            primary = this.selector.select(new URI((secure ? "https" : "http") + "://" + this.host));
            secondary = this.selector.select(new URI((secure ? "http" : "https") + "://" + this.host));
        } catch (@Pc(95) URISyntaxException local95) {
            return this.create();
        }

        primary.addAll(secondary);
        @Pc(106) Object[] proxies = primary.toArray();
        @Pc(108) ProxyAuthenticationException authenticationException = null;
        for (@Pc(112) int i = 0; i < proxies.length; i++) {
            @Pc(124) Object object = proxies[i];
            @Pc(127) Proxy proxy = (Proxy) object;

            try {
                @Pc(132) Socket proxySocket = this.proxyConnect(proxy);

                if (proxySocket != null) {
                    System.out.println("Connected with: " + proxy);
                    return proxySocket;
                }
            } catch (@Pc(139) ProxyAuthenticationException exception) {
                System.out.println("Proxy auth failure: " + exception.getMessage());
                authenticationException = exception;
            } catch (@Pc(143) IOException local143) {
                /* empty */
            }

            System.out.println("Failed to connect with: " + proxy);
        }

        if (authenticationException == null) {
            return this.create();
        } else {
            throw authenticationException;
        }
    }
}
