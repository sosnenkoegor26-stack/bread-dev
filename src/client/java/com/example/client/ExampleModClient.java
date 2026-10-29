package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleModClient implements ClientModInitializer {
private static final Logger LOGGER = Logger.getLogger("bread");

@Override
public void onInitializeClient() {
Path configPath = FabricLoader.getInstance()
.getConfigDir()
.resolve("bread.properties");

Properties config = new Properties();
config.setProperty("diagnostics.enabled", "true");

try {
if (Files.exists(configPath)) {
try (Reader reader = Files.newBufferedReader(
configPath, StandardCharsets.UTF_8)) {
config.load(reader);
}
} else {
Files.createDirectories(configPath.getParent());

try (Writer writer = Files.newBufferedWriter(
configPath, StandardCharsets.UTF_8)) {
config.store(writer, "bread stage 1 settings");
}
}
} catch (IOException | IllegalArgumentException exception) {
LOGGER.log(
Level.WARNING,
"[bread] Cannot load settings; using defaults",
exception
);
}

LOGGER.info("[bread] Stage 1 initialized. Renderer unchanged.");

boolean diagnosticsEnabled = Boolean.parseBoolean(
config.getProperty("diagnostics.enabled", "true")
);

if (!diagnosticsEnabled) {
LOGGER.info("[bread] Startup diagnostics disabled.");
return;
}

Runtime runtime = Runtime.getRuntime();
long usedMemory = runtime.totalMemory() - runtime.freeMemory();
long maxMemory = runtime.maxMemory();

LOGGER.info("[bread] Java: "
+ System.getProperty("java.version"));
LOGGER.info("[bread] OS: "
+ System.getProperty("os.name") + " / "
+ System.getProperty("os.arch"));
LOGGER.info("[bread] Available processors: "
+ runtime.availableProcessors());
LOGGER.info("[bread] Java heap used at startup: "
+ toMiB(usedMemory) + " MiB");
LOGGER.info("[bread] Java heap maximum: "
+ toMiB(maxMemory) + " MiB");
}

private static long toMiB(long bytes) {
return bytes / (1024L * 1024L);
}
}
