import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Demo {

	// Permet de lire des chaines de caractère en faisant readline sur la console
	private static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws Exception {

		// Chargement du fichier de configuration
		Properties props = new Properties();

		try (FileInputStream fis = new FileInputStream("conf.properties")) {

			props.load(fis);

		}

		// Instanciation du driver en mémoire
		Class.forName(props.getProperty("jdbc.driver.class"));

		String url = props.getProperty("jdbc.url");
		String dbLogin = props.getProperty("jdbc.login");
		String dbPassword = props.getProperty("jdbc.password");

		// Création d'une connexion
		try (Connection connection = DriverManager.getConnection(url, dbLogin, dbPassword)) {

			String readedLogin = "";
			System.out.println("---------------- WebStoreTest v1.0 ----------------");

			while (true) {

				// Champs de saisie des informations de connexion
				System.out.print("Login : ");
				String login = keyboard.readLine();
				System.out.print("Password : ");
				String password = keyboard.readLine();

				// Select avec les informations récupérées
				String strSql = "SELECT * FROM T_Users WHERE login= '" + login + "' AND password='" + password + "'";

				try (Statement statement = connection.createStatement();
						ResultSet resultSet = statement.executeQuery(strSql)) {

					// Si l'utilisateur n'existe pas, le next renvoie false
					if (resultSet.next()) {

						// Ajoute une connexion si l'utilisateur se connecte
						strSql = "UPDATE T_Users SET connectionNumber=connectionNumber+1 WHERE idUser = "
								+ resultSet.getInt("idUser");

						try (Statement stUpdate = connection.createStatement()) {

							stUpdate.executeUpdate(strSql);

						}

						// Sort de la boucle si l'utilisateur est connecté
						readedLogin = resultSet.getString("login");
						break;
					}

					System.out.println("Wrong password !");

				}

			}
			
			System.out.println("Welcome " + readedLogin + " !");

//			String strSql = "INSERT INTO T_Users (idUser, login, password, connectionNumber) "
//					+ "VALUES (6, 'Bourne', 'Jason', 8);";
//			
//			try (Statement statement = connection.createStatement()){
//				
//				statement.executeUpdate(strSql);
//				
//			}
//
//			String strSql = "SELECT * FROM T_Users;";
//
//			// Création du statement et du resultset dans le même try-with-ressouces
//			try (Statement statement = connection.createStatement();
//					ResultSet resultSet = statement.executeQuery(strSql)) {
//
//				while (resultSet.next()) {
//
//					// Récupération des éléments
//					int rsIdUser = resultSet.getInt(1);
//					String rsLogin = resultSet.getString(2);
//					String rsPassword = resultSet.getString("password");
//					int rsConnectionNumber = resultSet.getInt("connectionNumber");
//
//					System.out.printf("%d: %s %s - %d\n", rsIdUser, rsLogin, rsPassword, rsConnectionNumber);
//
//				}
//
//			}

		}

	}

}
