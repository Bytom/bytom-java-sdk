package io.bytom.api;

import io.bytom.common.Utils;
import org.bouncycastle.util.encoders.Hex;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Output {

    /**
     * The number of units of the asset being controlled.
     */
    private Long amount;

    /**
     * The id of the asset being controlled.
     */
    private String assetId;

    /**
     * The control program which must be satisfied to transfer this output.
     */
    private String controlProgram;

    /**
     * The id of the output.
     */
    private String id;

    /**
     * The output's position in a transaction's list of outputs.
     */
    private Integer position;

    public Output(String assetId, Long amount, String controlProgram) {
        this.assetId = assetId;
        this.amount = amount;
        this.controlProgram = controlProgram;
    }

    public byte[] serializeOutput() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //assetVersion
        Utils.writeVarint(1, stream); //AssetVersion是否默认为1
        //outputCommit
        ByteArrayOutputStream outputCommitSteam = new ByteArrayOutputStream();
        //assetId
        outputCommitSteam.write(Hex.decode(assetId));
        //amount
        Utils.writeVarint(amount, outputCommitSteam);
        //vmVersion
        Utils.writeVarint(1, outputCommitSteam); //db中获取vm_version
        //controlProgram
        Utils.writeVarStr(Hex.decode(controlProgram), outputCommitSteam);

        byte[] dataOutputCommit = outputCommitSteam.toByteArray();
        //outputCommit的length
        Utils.writeVarint(dataOutputCommit.length, stream);
        stream.write(dataOutputCommit);

        //outputWitness
        Utils.writeVarint(0, stream);
        return stream.toByteArray();
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getControlProgram() {
        return controlProgram;
    }

    public void setControlProgram(String controlProgram) {
        this.controlProgram = controlProgram;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
