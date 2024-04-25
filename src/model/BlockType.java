package model;

public enum BlockType {
    IBlock,
    JBlock,
    LBlock,
    OBlock,
    SBlock,
    TBlock,
    ZBlock,
    NullBlock,
    WeightItemBlock,
    BombItemBlock,
    ExtensionItemBlock,
    ItemBlock;
    public static BlockType getBlockTypeByIndex(int index) {
        return values()[index];
    }
}
