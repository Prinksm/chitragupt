package com.example.demo.emails.template;

import org.springframework.beans.factory.annotation.Autowired;

public class GlobalTemplate {
    @Autowired

    private static final String LOGO_URL = "http://localhost:8089/images/img.png";

    public static String wrap(String contentHtml) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px;">
                    <div style="
                        max-width: 600px;
                        margin: auto;
                        background: #ffffff;
                        border-radius: 8px;
                        padding: 20px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                    ">

                        <h2 style="color:#2d3748; text-align: center; margin-top: 0;">
                            Chitragupt
                        </h2>

                        <div style="font-size: 15px; color:#333; line-height:1.6; margin-top: 20px;">
                            %s
                        </div>

                        <p style="margin-top: 30px; font-size: 13px; color: #777;">
                            This is an automated email. Please do not reply.
                        </p>
                    </div>
                </body>
                </html>
                """.formatted(contentHtml);
    }
}
