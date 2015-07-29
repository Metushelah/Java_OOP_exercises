package oop.ex6.logic_analysis.variables;

import java.util.regex.Pattern;

/**
 * An Enum representing the different variables we can have in the program. Each case holds with 
 * himself it's correct pattern of values, it's reserved word and a default value.
 * 
 * Created by orrp and guybrush on 6/14/15.
 */
public enum VarType {

	/* Because Boolean can hold Double and in turn can hold Int we thought of trying to reuse the 
	 * pattern but it's not possible because private static finals may not be declared before enum.
	 */
	/**
	 * holds the Integer var type.
	 */
    INT ("^-?\\d++$", "int", "0"),
    /**
	 * holds the Double var type.
	 */
    DOUBLE ("^-?\\d++(?:\\.\\d++)?$", "double", "0"),
    /**
	 * holds the Boolean var type.
	 */
    BOOLEAN("^(?:true|false|-?\\d++|-?\\d++(?:\\.\\d++))$", "boolean", "false"),
    /**
	 * holds the String var type.
	 */
    STRING ("\"[^\"]*\"", "String", "\"\""),
    /**
	 * holds the Char var type.
	 */
    CHAR("^\'.\'$", "char", "'a'");

    private final Pattern pattern;
    private final String name;
    private final String defaultValue;

    /* main constructor */
    private VarType (String valueRegex, String typeName, String defaultValue){
        pattern = Pattern.compile(valueRegex);
        name = typeName;
        this.defaultValue = defaultValue; //Default value, sometimes used as dummy value
    }

    /**
     * @return a pattern representing that Variable type.
     */
    public Pattern getPattern(){
        return pattern;
    }

    /**
     * @return the type name.
     */
    String getName(){
        return name;
    }

    /**
     * @return a default value of that type.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * The purpose of this method is to check whether values or variables can pass on themselves 
     * correctly because of the different relations of boolean, double, int. 
     * @param other the other type that can be held inside yourself.
     * @return True or False if the types compare well.
     */
    public boolean isA(VarType other){
        switch (this){
            case INT:
                return other == INT;
            case DOUBLE:
                return other == DOUBLE || other.isA(INT);
            case STRING:
                return other == STRING;
            case BOOLEAN:
                return other == BOOLEAN || other.isA(DOUBLE);
            case CHAR:
                return other == CHAR;
            default:
                throw new IllegalStateException(); // Called an isA from something that isn't VarType
        }
    }

    /**
     *  Turn a string into a VarType. Returns null if type not found.
     *  @param string the var type string if correct.
     *  @return a VarType, one of the enums if applicable.
     */
    public static VarType stringToType(String string){
        if (string == null){
            return null;
        }
        for (VarType type : VarType.values()){
            if (type.getName().equals(string)){
                return type;
            }
        }
        return null;
    }

    /**
     * Create a literal value out of String value to be used when assigning a variable.
     * Note that order matters here, due to the transitive property (boolean isA double, etc.).
     * @param value the value to turn to literal.
     * @return a VarType of the value given. e.g. "Hello" will return String.
     */
    public static VarType literal(String value){
        VarType type;
        if (VarType.STRING.getPattern().matcher(value).matches()){
            type = VarType.STRING;
        }
        else if (VarType.CHAR.getPattern().matcher(value).matches()){
            type = VarType.CHAR;
        }
        else if (VarType.INT.getPattern().matcher(value).matches()){
            type = VarType.INT;
        }
        else if (VarType.DOUBLE.getPattern().matcher(value).matches()){
            type = VarType.DOUBLE;
        }else if (VarType.BOOLEAN.getPattern().matcher(value).matches()){
            type = VarType.BOOLEAN;
        }
        else{
            return null;
        }
        return type;
    }

}
