package loginapp;
// options for login user type

public enum option {
    Admin, User;

    private option(){}

    public String value(){
        return name();
    }
    public static option fromvalue(String v){
        return valueOf(v);
    }
}
