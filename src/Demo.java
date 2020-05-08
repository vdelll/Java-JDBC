import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Demo {

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();

		try (FileInputStream fis = new FileInputStream("conf.properties")) {

			props.load(fis);

		}

		// Instanciation du driver en mémoire
		Class.forName(props.getProperty("jdbc.driver.class"));

		String url = props.getProperty("jdbc.url");
		String login = props.getProperty("jdbc.login");
		String password = props.getProperty("jdbc.password");

		// Création d'une connexion
		try (Connection connection = DriverManager.getConnection(url, login, password)) {

//			String strSql = "INSERT INTO T_Users (idUser, login, password, connectionNumber) "
//					+ "VALUES (6, 'Bourne', 'Jason', 8);";
//			
//			try (Statement statement = connection.createStatement()){
//				
//				statement.executeUpdate(strSql);
//				
//			}

			String strSql = "SELECT * FROM T_Users;";

			// Création du statement et du resultset dans le même try-with-ressouces
			try (Statement statement = connection.createStatement();
					ResultSet resultSet = statement.executeQuery(strSql)) {

				while (resultSet.next()) {

					// Récupération des éléments
					int rsIdUser = resultSet.getInt(1);
					String rsLogin = resultSet.getString(2);
					String rsPassword = resultSet.getString("password");
					int rsConnectionNumber = resultSet.getInt("connectionNumber");

					System.out.printf("%d: %s %s - %d\n", rsIdUser, rsLogin, rsPassword, rsConnectionNumber);

				}

			}

		}

	}

}
