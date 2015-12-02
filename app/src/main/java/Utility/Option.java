package Utility;

/**
 * Created by TUSHAR_SK on 11/28/15.
 */
public class Option implements Comparable<Option>{

    private String name;
    private String date;
    private String data;
    private String path;
    private String image;

    public Option(String n,String d,String dt, String p, String img)
    {
        name = n;
        data = d;
        path = p;
        date =dt;
        image =img;
    }

    public String getName()
    {
        return name;
    }

    public String getData()
    {
        return data;
    }

    public String getPath()
    {
        return path;
    }

    public String getDate()
    {
        return date;
    }

    public String getImage() {
        return image;
    }



    @Override
    public int compareTo(Option o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }

}
