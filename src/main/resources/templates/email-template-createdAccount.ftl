<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Activation de votre compte MBAL</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>

    <!-- use the font -->
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 16px;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">

<table align="center" border="0" cellpadding="0" cellspacing="0" width="600" style="border-collapse: collapse;">
    <tr>
        <td align="center" bgcolor="#78ab46" style="padding: 40px 0 30px 0;">
            <img src="cid:logo.png" alt="http://serveurpi.ddns.net/WebSocketClient" style="display: block;" />
        </td>
    </tr>
    <tr>
        <td bgcolor="#eaeaea" style="padding: 40px 30px 40px 30px;">
            <p>Bonjour ${name},</p>
            <p>Nous vous remercions de vous être inscrit sur notre plateforme <b>MBAL</b></p>
            <p>Veuillez cliquer sur <a href="http://serveurpi.ddns.net/WebSocketClient?activate=${id}">ce lien</a> pour finaliser votre inscription !</p>
            <p>Si votre compte n'est pas activé d'ici le <b>${date}</b> à <b>${hour}</b>, il sera automatiquement supprimé !</p>
            <p>A très vite !</p>
        </td>
    </tr>
    <tr>
        <td bgcolor="#777777" style="padding: 30px 30px 30px 30px;">
            <p>${signature}</p>
            <p>${location}</p>
        </td>
    </tr>
</table>

</body>
</html>