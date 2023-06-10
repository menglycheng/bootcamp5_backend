package com.checkme.CheckMe.email;

public class EmailEnableUserTemplate {
    public static String enableUserEmailTemplate(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Account Activation</title>\n" +
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
                "    <h2>Account Activation</h2>\n" +
                "    <p>Hello <strong>" + name + "</strong>,</p>\n" +
                "    <p>Thank you for registering an account with us. Your account has been created successfully.</p>\n" +
                "    <p>To activate your account, please click the following link:</p>\n" +
                "    <p>\n" +
                "        <a href=\"" + link + "\" class=\"button\">Activate Account</a>\n" +
                "    </p>\n" +
                "    <p>\n" +
                "        Note: This link will expire in 15 minutes.\n" +
                "    </p>\n" +
                "        <p>Thank you!</p>\n" +
                "    </p>\n" +
                "</body>\n" +
                "</html>";
    }
}
