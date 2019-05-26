package com.mizoz.gmachine.processing;

public class Instruction {
    
    public Instruction(String label, String instruction, String args, String comment){
        this.label = label;
        this.instruction = instruction;
        this.args = args;
        this.comment = comment;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
    
    private String label, instruction, args, comment;
}
