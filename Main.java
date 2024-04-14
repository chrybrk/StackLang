// stack based language
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// main class
public class Main {
    public static Reader reader;
    public static Stack stack = new Stack();

    public static void main(String[] args) {
        Main instance = new Main();

        if (args[0].compareTo("file") == 0) {
            instance.FromFile(args[1]);
        } else {
            instance.FromInput();
        }
    }

    public void AlterStack(String data) {
        if (isNumeric(data))
            stack.Push((int)Double.parseDouble(data));
        else {
            switch (data) {
                case "+": {
                    System.out.println("[INFO]: found `+` operation.");
                    int lop = stack.Access(2);
                    int rop = stack.Access(1);
                    stack.Pop();
                    stack.Pop();
                    stack.Push(lop + rop);
                    break;
                }
                case "-": {
                    System.out.println("[INFO]: found `-` operation.");
                    int lop = stack.Access(2);
                    int rop = stack.Access(1);
                    stack.Pop();
                    stack.Pop();
                    stack.Push(lop - rop);
                    break;
                }
                case "*": {
                    System.out.println("[INFO]: found `*` operation.");
                    int lop = stack.Access(2);
                    int rop = stack.Access(1);
                    stack.Pop();
                    stack.Pop();
                    stack.Push(lop * rop);
                    break;
                }
                case "/": {
                    System.out.println("[INFO]: found `/` operation.");
                    int lop = stack.Access(2);
                    int rop = stack.Access(1);
                    stack.Pop();
                    stack.Pop();
                    stack.Push(lop / rop);
                    break;
                }
                case "%": {
                    System.out.println("[INFO]: found `%` operation.");
                    int lop = stack.Access(2);
                    int rop = stack.Access(1);
                    stack.Pop();
                    stack.Pop();
                    stack.Push(lop % rop);
                    break;
                }
                case "dup": {
                    System.out.println("[INFO]: found `dup` operation.");
                    stack.Push(stack.Access(1));
                    break;
                }
                case "drop": {
                    System.out.println("[INFO]: found `drop` operation.");
                    stack.Pop();
                    break;
                }
                case "swap": {
                    System.out.println("[INFO]: found `swap` operation.");
                    int lop = stack.Access(2);
                    int rop = stack.Access(1);
                    stack.Pop();
                    stack.Pop();
                    stack.Push(rop);
                    stack.Push(lop);
                    break;
                }
                case "max": {
                    int lop = stack.Access(2);
                    int rop = stack.Access(1);
                    stack.Push(lop > rop ? lop : rop);
                    break;
                }
                case "min": {
                    int lop = stack.Access(2);
                    int rop = stack.Access(1);
                    stack.Push(lop < rop ? lop : rop);
                    break;
                }
                case "cr": {
                    System.out.println("[INFO]: found `cr` operation.");
                    System.out.println("[INFO]: STACK_OUT :: " + stack.Pop());
                    break;
                }
            }
        }
    }

    public void FromFile(String path) {
        reader = new Reader(path);

        String[] s = reader.GetData().trim().split("\\s+");
        for (int i = 0; i < s.length; i++) {
            this.AlterStack(s[i]);
        }

        stack.Viewport(0, 10);
    }

    public void FromInput() {
        Scanner new_scanner = new Scanner(System.in);
        while (true) {
            System.out.print("[INFO]: input :: ");
            String bit = new_scanner.next();
            AlterStack(bit);
            stack.Viewport(stack.GetIndex() - 5, stack.GetIndex() + 5);
        }
    }

    public static boolean isNumeric(String str) { 
        try {  
            Double.parseDouble(str);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }
}

// stack: core
public class Stack {
    // 64KB stack
    public final static int STACK_SIZE = 1024 * 64;
    private int[] _stack;
    private int _index;

    public Stack() {
        _stack = new int[STACK_SIZE];
        _index = 0;

        // set stack to null
        for (int i = 0; i < STACK_SIZE; i++) _stack[i] = 0;
    }

    public void Push(int item) {
        _stack[_index] = item;
        _index++;
    }

    public int Pop() {
        _index--;
        int item = _stack[_index];
        _stack[_index] = 0;

        return item;
    }

    public int Access(int position) {
        if (position <= -1 || position >= STACK_SIZE) {
            System.out.println("[ERR]: Segmentation fault, cannot access stack beyond scope.");
            System.exit(-1);
        }

        return _stack[_index - position];
    }

    public int GetIndex() {
        return _index;
    }

    public void Viewport(int start, int end) {
        if (start <= -1) start = 0;
        if (end >= STACK_SIZE) end = STACK_SIZE - 1;

        System.out.println("--------------------------");
        System.out.println("STACK: " + (STACK_SIZE - _index) + "/" + STACK_SIZE + " available");
        for (int i = start; i < end; i++) {
            System.out.println("-- index: " + i + ", value: " + _stack[i]);
        }
        System.out.println("--------------------------");
        System.out.println("--------------------------");
    }
}

// it reads file: helper class
public class Reader {
    private String _filepath;
    private String _data;
    private long _size;

    public Reader(String filepath) {
        _filepath = filepath;
        _data = "";

        // try to open file
        try {
            File new_file = new File(_filepath);
            Scanner reader = new Scanner(new_file);

            _size = new_file.length();

            while (reader.hasNextLine())
                _data = _data.concat(reader.nextLine() + "\n");

            reader.close();

        } catch (FileNotFoundException err_log) {
            System.out.println("[ERR]: Failed to open file.");
            err_log.printStackTrace();
        }
    }

    public String GetData() {
        return _data;
    }

    public int GetLength() {
        return _data.length();
    }

    public long GetSize() {
        return _size;
    }
}
