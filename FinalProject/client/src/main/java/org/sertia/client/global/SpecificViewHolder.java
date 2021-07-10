package org.sertia.client.global;

public class SpecificViewHolder {
    public static final SpecificViewHolder instance = new SpecificViewHolder();
    private String branchName;
    private boolean isInitialized = false;

    public static SpecificViewHolder getInstance() {
        return instance;
    }

    public void initializeSpecificBranch(String branchName) {
        this.branchName = branchName;
        isInitialized = true;
    }

    public String getBranchName() {
        return branchName;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void clear() {
        isInitialized = false;
        branchName = null;
    }
}
