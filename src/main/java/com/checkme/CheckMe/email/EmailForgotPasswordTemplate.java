package com.checkme.CheckMe.email;

public class EmailForgotPasswordTemplate {
    public static String forgotPasswordEmailTemplate(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Email Confirmation</title>\n" +
                "    <style>\n" +
                "        /* Define button styles */\n" +
                "        .button {\n" +
                "            background-color: #3498db;\n" +
                "            border: none;\n" +
                "            color: white;\n" +
                "            padding: 12px 24px;\n" +
                "            text-align: center;\n" +
                "            text-decoration: none;\n" +
                "            display: inline-block;\n" +
                "            font-size: 16px;\n" +
                "            margin: 4px 2px;\n" +
                "            cursor: pointer;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h2>Forgot Password</h2>\n" +
                "    <p>Hello <strong>" + name + "</strong>,</p>\n" +
                "    <p>We received a request to reset your password. If you did not initiate this request, please ignore this email.</p>\n" +
                "    <p>If you did request to reset your password, please click on the button below:</p>\n" +
                "    <p>\n" +
                "        <a href=\"" + link + "\" class=\"button\">Reset Password</a>\n" +
                "    </p>\n" +
                "        Note: This link will expire in 15 minutes.\n" +
                "    <p>\n" +
                "        <p>Thank you!</p>\n" +
                "    </p>\n" +
                "</body>\n" +
                "</html>";
    }
}
