package cn.zay.zayboot.constant;

/**
 * @author ZAY
 */
public final class ServerBanner {
    public static final String LOG_PORT_BANNER = """
            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            *                                                                                   *
            *                                                                                   *
            *                   Netty Http Server started on port {}.                           *
            *                                                                                   *
            *                                                                                   *
            * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
            """;
    private ServerBanner() {}
}
