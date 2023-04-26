package Backend;


import com.google.gson.Gson;
import com.mpatric.mp3agic.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args){


        try{
            File archtext=new File("E:\\FFOutputMusic\\TechHouseInfo.txt");
            File pathMusic=new File("E:\\FFOutputMusic\\Tech-House");
            Map<Integer,String> datosCanciones=new HashMap<>();
            FileWriter fw = new FileWriter(archtext.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            Buscador(pathMusic,datosCanciones);
            Gson gson=new Gson();
            String jsonString = gson.toJson(datosCanciones);
            bw.write(jsonString);
            bw.close();

            System.out.println("se guardo la info de la musica");
        }catch (IOException | InvalidDataException | UnsupportedTagException e){
            e.printStackTrace();
        }


    }
    public static void Buscador(File direccion,Map<Integer, String> datosCanciones) throws InvalidDataException, UnsupportedTagException, IOException {

        if (direccion.isDirectory()) {
            File[] dire=direccion.listFiles();
            assert dire != null;
            for (File archivoActual : dire) {
                if(archivoActual.isDirectory()){
                    Buscador(archivoActual,datosCanciones);
                }

                else if (archivoActual.isFile() && archivoActual.getName().endsWith(".mp3")) {
                    Mp3File mp3file = new Mp3File(archivoActual.getAbsoluteFile());
                    ArrayList<String> metadata = new ArrayList<>();
                    if (mp3file.hasId3v1Tag()) {
                        ID3v1 id3v1tag = mp3file.getId3v1Tag();
                        metadata.add("Artista: " + id3v1tag.getArtist());
                        metadata.add("Canción: " + id3v1tag.getTitle());
                        metadata.add("Álbum: " + id3v1tag.getAlbum());
                        metadata.add("Año: " + id3v1tag.getYear());

                    } else if (mp3file.hasId3v2Tag()) {
                        ID3v2 id3v2tag = mp3file.getId3v2Tag();
                        metadata.add("Artista: " + id3v2tag.getArtist());
                        metadata.add("Canción: " + id3v2tag.getTitle());
                        metadata.add("Álbum: " + id3v2tag.getAlbum());
                        metadata.add("Año: " + id3v2tag.getYear());
                    } else {
                        System.out.println("El archivo no contiene metadatos ID3.");
                    }
                    String dataString=String.join(" ",metadata);
                    System.out.println(dataString);
                    datosCanciones.put(datosCanciones.size()+1, dataString);

                }

            }
        }

    }
}
