package com.evernightfireworks.mcci;

import com.evernightfireworks.mcci.blocks.CausalBlocks;
import net.fabricmc.api.ClientModInitializer;

public class CausalEngineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CausalBlocks.registerClient();
    }
}