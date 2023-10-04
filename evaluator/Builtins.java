package evaluator;

import object.Object;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Builtins {
    public static Object.BuiltinFunction LEN = (Object.object... args) -> {
        if(Objects.equals(args.length,1)) {
            return new Object.Error(String.format("wrong number of arguments. got=%d, want=1",
                    args.length));
        }

        return switch (args[0]) {
            case Object.Array array -> new Object.Integer((array.Elements.size()));
            case Object.String string ->  new Object.Integer((string.Value.length()));
            case Object.Hash hash -> new Object.Integer(hash.Pairs.size());
            default -> new Object.Error(String.format("argument to 'len' not supported, got %s", args[0].Type()));
        };
    };

    public static Object.BuiltinFunction PUTS = (Object.object... args) -> {
        for(var s : args) {
            System.out.println(s.Inspect());
        }

        return new Object.Null();
    };

    public static Object.BuiltinFunction FIRST = (Object.object... args) -> {
        if(!Objects.equals(args.length,1)) {
            return new Object.Error(String.format("wrong number of arguments. got=%d, want=1",
                    args.length));
        }
        if(!Objects.equals(args[0].Type(),Object.ARRAY_OBJ)) {
            return new Object.Error(String.format("argument to 'first' must be ARRAY, got %s",
                    args[0].Type()));
        }

        var arr = (Object.Array)args[0];
        if(!arr.Elements.isEmpty()) {
            return arr.Elements.get(0);
        }

        return new Object.Null();
    };

    public static Object.BuiltinFunction LAST = (Object.object... args) -> {
        if(!Objects.equals(args.length,1)) {
            return new Object.Error(String.format("wrong number of arguments. got=%d, want=1",
                    args.length));
        }
        if(!Objects.equals(args[0].Type(),Object.ARRAY_OBJ)) {
            return new Object.Error(String.format("argument to 'first' must be ARRAY, got %s",
                    args[0].Type()));
        }

        var arr = (Object.Array)args[0];
        var length = arr.Elements.size();
        if(!arr.Elements.isEmpty()) {
            return arr.Elements.get(length - 1);
        }

        return new Object.Null();
    };

    public static Object.BuiltinFunction REST = (Object.object... args) -> {
        if(!Objects.equals(args.length,1)) {
            return new Object.Error(String.format("wrong number of arguments. got=%d, want=1",
                    args.length));
        }
        if(!Objects.equals(args[0].Type(),Object.ARRAY_OBJ)) {
            return new Object.Error(String.format("arguments to 'rest' must be ARRAY, got %s",
                    args.length));
        }

        var arr = (Object.Array)args[0];
        var length = arr.Elements.size();
        if(length > 0) {
            var newElements = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
            return new Object.Array(newElements);
        }

        return new Object.Null();
    };

    public static Object.BuiltinFunction PUSH = (Object.object... args) -> {
        if(!Objects.equals(args.length,2)) {
            return new Object.Error(String.format("wrong number of arguments. got=%d, want=2",
                    args.length));
        }
        if(!Objects.equals(args[0].Type(),Object.ARRAY_OBJ)) {
            return new Object.Error(String.format("argument to 'push' must be ARRAY, got %s",
                    args[0].Type()));
        }

        var arr = (Object.Array)args[0];
        var length = arr.Elements.size();

        var newElements = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(args, 0, args.length)));
        newElements.set(length,args[1]);

        return new Object.Array(newElements);
    };

    public static Object.Builtin len = new Object.Builtin(LEN);
    public static Object.Builtin puts = new Object.Builtin(PUTS);
    public static Object.Builtin first = new Object.Builtin(FIRST);
    public static Object.Builtin last = new Object.Builtin(LAST);
    public static Object.Builtin rest = new Object.Builtin(REST);
    public static Object.Builtin push = new Object.Builtin(PUSH);

    public static HashMap<String, Object.Builtin> builtins = new HashMap<>() {{
        put("len", len);
        put("puts", puts);
        put("first", first);
        put("last", last);
        put("rest", rest);
        put("push",push);
    }};
}
