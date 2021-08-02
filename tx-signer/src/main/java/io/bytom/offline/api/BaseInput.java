package io.bytom.offline.api;

import io.bytom.offline.common.Utils;
import io.bytom.offline.types.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public abstract class BaseInput {

    static final int ISSUANCE_INPUT_TYPE = 0;
    static final int SPEND_INPUT_TYPE = 1;
    static final int Coinbase_INPUT_TYPE = 2;
    static final int Veto_INPUT_TYPE = 3;

    static final byte AssetKeySpace = 0;
    static final byte AccountKeySpace = 1;

    private String inputID;

    /**
     * The number of units of the asset being issued or spent.
     */
    private Long amount;

    /**
     * The id of the asset being issued or spent.
     */
    private String assetId;

    /**
     * The program which must be satisfied to transfer this output.
     * it represents control program when is SpendInput, it represents issuance program when is IssuanceInput
     */
    private String program;

    private int vmVersion = 1;

    /**
     * used for bip32 derived path.
     * it represents accountIndex when is SpendInput, it represents assetIndex when is IssuanceInput
     */
    private Integer keyIndex;

    WitnessComponent witnessComponent = new WitnessComponent();

    public BaseInput() {}

    public abstract InputEntry toInputEntry(Map<Hash, Entry> entryMap, int index);
    public abstract void buildWitness(String txID) throws Exception;
    public abstract byte[] serializeInputCommitment() throws IOException;
    public abstract byte[] serializeInputWitness() throws IOException;

    public byte[] serializeInput() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // asset version
        Utils.writeVarint(1, stream);
        Utils.writeExtensibleString(serializeInputCommitment(), stream);
        Utils.writeExtensibleString(serializeInputWitness(), stream);
        return stream.toByteArray();
    }


    public void validate() {
        if (assetId == null) {
            throw new IllegalArgumentException("the asset id of input must be specified.");
        }
        if (amount == null) {
            throw new IllegalArgumentException("the amount id of input must be specified.");
        }
        if (program == null) {
            throw new IllegalArgumentException("the program id of input must be specified.");
        }
        if (keyIndex == null) {
            throw new IllegalArgumentException("the key index of input must be specified.");
        }
        if (witnessComponent.getRootPrivateKey() == null) {
            throw new IllegalArgumentException("the root private key of input must be specified.");
        }
    }

    public AssetAmount getAssetAmount() {
        return new AssetAmount(new AssetID(this.assetId), this.amount);
    }

    public void setRootPrivateKey(String prvKey) {
        witnessComponent.setRootPrivateKey(prvKey);
    }

    public String getInputID() {
        return inputID;
    }

    public void setInputID(String inputID) {
        this.inputID = inputID;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getVmVersion() {
        return vmVersion;
    }

    public void setVmVersion(int vmVersion) {
        this.vmVersion = vmVersion;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
    }

    public void appendWitnessComponent(String witness){
        witnessComponent.appendWitness(witness);
    }
}
