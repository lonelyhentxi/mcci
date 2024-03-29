package com.evernightfireworks.mcci;

import com.evernightfireworks.mcci.blocks.CausalBlocks;
import com.evernightfireworks.mcci.services.CausalService;
import com.evernightfireworks.mcci.services.CraftingPolicyService;
import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CausalEngine implements ModInitializer {
    private static final Logger logger = LogManager.getFormatterLogger(CausalEngine.class.getName());
    public static CraftingPolicyService CRAFTING_POLICY_SERVICE;
    public static CausalService CAUSAL_SERVICE;

    @Override
    public void onInitialize() {
        logger.info("Causal engine initializing...");
        CausalBlocks.registerMain();
        CraftingPolicyService.registerMain();
        CRAFTING_POLICY_SERVICE = new CraftingPolicyService();
        CAUSAL_SERVICE = new CausalService();
        logger.info("Causal engine initialized");
    }
}
