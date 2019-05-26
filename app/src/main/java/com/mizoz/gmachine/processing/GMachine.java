package com.mizoz.gmachine.processing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class GMachine {

    public GMachine(ArrayList<String> input, HashMap<BigInteger, BigInteger> memory, HashMap<BigInteger, Instruction> instructions) {
        this.input = input;
        this.memory = memory;
        this.instructions = instructions;
        labelToAddress = new HashMap<>();
        rPointer = 0;
        iPointer = BigInteger.ZERO;
        sReturn = "";
    }

    public void init() throws GExecutionException {
        labelToAddress.clear();
        sReturn = "";
        for (HashMap.Entry<BigInteger, Instruction> instruction : instructions.entrySet()) {
            if (instruction.getValue().getLabel() != null && !instruction.getValue().getLabel().trim().equals("")) {
                if (labelToAddress.get(instruction.getValue().getLabel().trim()) == null) {
                    labelToAddress.put(instruction.getValue().getLabel().trim(), instruction.getKey());
                } else {
                    throw new GExecutionException("Label: " + instruction.getValue().getLabel().trim() + " Used Multiple Times!");
                }
            }
        }
        rPointer = 0;
        iPointer = BigInteger.ZERO;

    }

    public BigInteger getInstructionPointer(){
        return iPointer;
    }

    public boolean process() throws GExecutionException {
        Instruction i = instructions.get(iPointer);
        iPointer = iPointer.add(BigInteger.ONE);
        sReturn = "";
        if (i == null) {
            return true;
        }

        switch (i.getInstruction()) {
            case "load": {
                if (i.getArgs().startsWith("=")) {
                    try {
                        BigInteger integer = new BigInteger(i.getArgs().substring(1));
                        memory.put(BigInteger.ZERO, integer);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                } else {
                    try {
                        BigInteger integer = new BigInteger(i.getArgs());
                        memory.put(BigInteger.ZERO, memory.get(integer));
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                }
                break;
            }
            case "store": {
                try {
                    BigInteger integer = new BigInteger(i.getArgs());
                    memory.put(integer, memory.get(BigInteger.ZERO));
                } catch (NumberFormatException ex) {
                    throw new GExecutionException("Invalid Number: " + i.getArgs());
                }
                break;
            }
            case "read": {
                try {
                    BigInteger integer = new BigInteger(input.get(rPointer));
                    BigInteger args = new BigInteger(i.getArgs());
                    rPointer++;
                    memory.put(args, integer);
                } catch (NumberFormatException ex) {
                    throw new GExecutionException("Invalid Number: " + i.getArgs());
                } catch (IndexOutOfBoundsException ex) {
                    throw new GExecutionException("No Input");
                }
                break;
            }
            case "write": {
                if (i.getArgs().startsWith("=")) {
                    try {
                        BigInteger integer = new BigInteger(i.getArgs().substring(1));
                        sReturn = integer.toString();
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                } else {
                    try {
                        BigInteger integer = new BigInteger(i.getArgs());
                        sReturn = ((memory.get(integer) != null ? memory.get(integer) : BigInteger.ZERO).toString());
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                }
                break;
            }
            case "add": {
                if (i.getArgs().startsWith("=")) {
                    try {
                        BigInteger integer = new BigInteger(i.getArgs().substring(1));
                        BigInteger a = (memory.get(BigInteger.ZERO) != null ? memory.get(BigInteger.ZERO) : BigInteger.ZERO);
                        BigInteger c = a.add(integer);
                        memory.put(BigInteger.ZERO, c);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                } else {
                    try {
                        BigInteger integerAddress = new BigInteger(i.getArgs());
                        BigInteger a = (memory.get(BigInteger.ZERO) != null ? memory.get(BigInteger.ZERO) : BigInteger.ZERO);
                        BigInteger b = (memory.get(integerAddress) != null ? memory.get(integerAddress) : BigInteger.ZERO);
                        BigInteger c = a.add(b);
                        memory.put(BigInteger.ZERO, c);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                }
                break;
            }
            case "sub": {
                if (i.getArgs().startsWith("=")) {
                    try {
                        BigInteger integer = new BigInteger(i.getArgs().substring(1));
                        BigInteger a = (memory.get(BigInteger.ZERO) != null ? memory.get(BigInteger.ZERO) : BigInteger.ZERO);
                        BigInteger c = a.subtract(integer);
                        memory.put(BigInteger.ZERO, c);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                } else {
                    try {
                        BigInteger integerAddress = new BigInteger(i.getArgs());
                        BigInteger a = (memory.get(BigInteger.ZERO) != null ? memory.get(BigInteger.ZERO) : BigInteger.ZERO);
                        BigInteger b = (memory.get(integerAddress) != null ? memory.get(integerAddress) : BigInteger.ZERO);
                        BigInteger c = a.subtract(b);
                        memory.put(BigInteger.ZERO, c);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                }
                break;
            }
            case "mult": {
                if (i.getArgs().startsWith("=")) {
                    try {
                        BigInteger integer = new BigInteger(i.getArgs().substring(1));
                        BigInteger a = (memory.get(BigInteger.ZERO) != null ? memory.get(BigInteger.ZERO) : BigInteger.ZERO);
                        BigInteger c = a.multiply(integer);
                        memory.put(BigInteger.ZERO, c);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                } else {
                    try {
                        BigInteger integerAddress = new BigInteger(i.getArgs());
                        BigInteger a = (memory.get(BigInteger.ZERO) != null ? memory.get(BigInteger.ZERO) : BigInteger.ZERO);
                        BigInteger b = (memory.get(integerAddress) != null ? memory.get(integerAddress) : BigInteger.ZERO);
                        BigInteger c = a.multiply(b);
                        memory.put(BigInteger.ZERO, c);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    }
                }
                break;
            }
            case "div": {
                if (i.getArgs().startsWith("=")) {
                    try {
                        BigInteger integer = new BigInteger(i.getArgs().substring(1));
                        BigInteger a = (memory.get(BigInteger.ZERO) != null ? memory.get(BigInteger.ZERO) : BigInteger.ZERO);
                        BigInteger c = a.divide(integer);
                        memory.put(BigInteger.ZERO, c);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    } catch (ArithmeticException ex) {
                        throw new GExecutionException("Div/0: " + i.getArgs());
                    }
                } else {
                    try {
                        BigInteger integerAddress = new BigInteger(i.getArgs());
                        BigInteger a = (memory.get(BigInteger.ZERO) != null ? memory.get(BigInteger.ZERO) : BigInteger.ZERO);
                        BigInteger b = (memory.get(integerAddress) != null ? memory.get(integerAddress) : BigInteger.ZERO);
                        BigInteger c = a.divide(b);
                        memory.put(BigInteger.ZERO, c);
                    } catch (NumberFormatException ex) {
                        throw new GExecutionException("Invalid Number: " + i.getArgs());
                    } catch (ArithmeticException ex) {
                        throw new GExecutionException("Div/0: " + i.getArgs());
                    }
                }
                break;
            }
            case "jump": {
                BigInteger address = labelToAddress.get(i.getArgs());
                if (address == null) {
                    throw new GExecutionException("No Such Label: " + i.getArgs());
                }
                iPointer = address;
                break;
            }
            case "jzero": {
                if (memory.get(BigInteger.ZERO) == null || memory.get(BigInteger.ZERO).equals(BigInteger.ZERO)) {
                    BigInteger address = labelToAddress.get(i.getArgs());
                    if (address == null) {
                        throw new GExecutionException("No Such Label: " + i.getArgs());
                    }
                    iPointer = address;
                }
                break;
            }
            case "jgtz": {
                BigInteger iZero = memory.get(BigInteger.ZERO);
                if (iZero == null)
                    break;
                if ((BigInteger.ZERO).compareTo(iZero) < 0) {
                    BigInteger address = labelToAddress.get(i.getArgs());
                    if (address == null) {
                        throw new GExecutionException("No Such Label: " + i.getArgs());
                    }
                    iPointer = address;
                }
                break;
            }
            case "halt": {
                return true;
            }
            default: {
                throw new GExecutionException("Instruction: " + i.getInstruction() + " Not Defined!");
            }
        }
        return false;
    }

    public String getLastOutput(){
        return sReturn;
    }


    private int rPointer;
    private String sReturn;
    private BigInteger iPointer;
    private HashMap<String, BigInteger> labelToAddress;
    private ArrayList<String> input;
    private HashMap<BigInteger, BigInteger> memory;
    private HashMap<BigInteger, Instruction> instructions;
}
