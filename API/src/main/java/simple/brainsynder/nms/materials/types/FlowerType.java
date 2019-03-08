package simple.brainsynder.nms.materials.types;

import simple.brainsynder.nms.materials.WrappedType;

public enum FlowerType implements WrappedType {
    DANDELION(-1),
    POPPY,
    BLUE_ORCHID,
    ALLIUM,
    AZURE_BLUET,
    RED_TULIP,
    ORANGE_TULIP,
    WHITE_TULIP,
    PINK_TULIP,
    OXEYE_DAISY;

    int data;
    FlowerType () {
        data = (ordinal()-1);
    }
    FlowerType (int data) {
        this.data = data;
    }


    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getData() {
        return data;
    }
}
