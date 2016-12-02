package com.aleksandr.aleksandrov.clinic;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class SendAppeal  {

    JSONObject jsonObj = null;

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs1;



    private String param = null, answer = null, error = null;
    URL url = null;
    HttpURLConnection urlConnection;


    public SendAppeal(String reqTag, String reqDate, String reqTime, String reqName, String reqEmail, String reqPhone, String reqDescription) {
        try {
            con = DriverManager.getConnection(Resources.URL, Resources.PROPERTIES);
            stmt = con.createStatement();
            // byte[] b = tfFirstName.getText().getBytes();
            // String m = b.toString();
            // System.out.println(arg0);
            String query = "insert into apeal (appspecialization_id, date, time, oname, oemail, ophone, odescription, personnel_id, status) values ('" + reqTag + "', '" + reqDate +  "', '" + reqTime + "', '" + reqName +"', '" + reqEmail + "', '" + reqPhone + "', '" + reqDescription + Resources.ID_APPRECEIVER +"');";

            stmt.executeUpdate(query);

        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            try {

                con.close();
                stmt.close();
                rs1.close();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        /**
         * Для использования php кода (требуется дополнительные файлы)
         */
//        try {
//            url = new URL(Resources.URL_SEND_APPEAL);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("POST");
//            param = Resources.TAG_ID_SPEC +"="+ reqTag +"&" + Resources.TAG_APP_DATE + "=" + reqDate+ "&" + Resources.TAG_APP_TIME + "=" + reqTime + "&" + Resources.TAG_APP_ONAME + "=" + reqName + "&" + Resources.TAG_APP_OEMAIL + "=" + reqEmail + "&" + Resources.TAG_APP_OPHONE + "=" +reqPhone + "&" + Resources.TAG_APP_DESCRIPTION + "=" +reqDescription + "&" + Resources.TAG_ID_APPRECEIVER +"=" + Resources.ID_APPRECEIVER;
//            urlConnection.setDoOutput(true);
//            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
//            wr.write(param);
//            wr.close();
//            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            jsonObj = new JSONObject(response.toString());
//
//            int success = jsonObj.getInt(Resources.TAG_SUCCESS);
//            if (success == 1) {
//                answer = reqDate + " в " +reqTime;
//            } else {
//                answer = "Вы НЕ записаны к врачу";
//            }
//        } catch (MalformedURLException e) {
//            //This exception is thrown when a program attempts to create an URL from an incorrect specification.
//            error = "Что-то полшо не так";
//            e.printStackTrace();
//        } catch (IOException e) {
//            error = "Не удалось подкулючится к серверу";
//            e.printStackTrace();
//        } catch (JSONException e) {
//            error = "Что-то полшо не так";
//            e.printStackTrace();
//        } finally {
//            urlConnection.disconnect();
//        }
    }
    public String answer() {
        return answer;
    }

    public String error() {
        return error;
    }
}