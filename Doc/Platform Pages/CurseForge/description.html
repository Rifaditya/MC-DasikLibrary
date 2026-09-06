<p align="center">
  <img src="https://raw.githubusercontent.com/Rifaditya/MC-DasikLibrary/master/Images/Dasik%20page%20banner.jpg" alt="Dasik Library Banner" width="85%">
</p>

<p align="center">
  <a href="https://www.curseforge.com/minecraft/mc-mods/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&amp;logo=fabric" alt="Requires Fabric API"></a>
  <img src="https://img.shields.io/badge/Architecture-Universal_Core_Library-blueviolet?style=for-the-badge" alt="Universal Core Library">
  <img src="https://img.shields.io/badge/Environment-Client_&amp;_Server-success?style=for-the-badge" alt="Client &amp; Server">
  <img src="https://img.shields.io/badge/Language-Java_25-orange?style=for-the-badge&amp;logo=java" alt="Java 25">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
  <img src="https://img.shields.io/badge/Minecraft-26.1.2+-brightgreen?style=for-the-badge" alt="Minecraft 26.1.2+">
</p>

<h2>🧠 Dasik Library</h2>

<blockquote><p><strong>&ldquo;The universal Hive Mind foundation powering autonomous animal genetics, dynamic GameRules, and high-performance flocking across Minecraft.&rdquo;</strong></p></blockquote>

<hr>

<h2>📖 Introduction</h2>

<p>Developing modern Minecraft Fabric mods often means facing recurring architectural roadblocks: registering dynamic GameRules on the fly without causing immutable registry lockouts, synchronizing animal genetics and scale attributes across world save cycles, orchestrating swarming and flocking AI without decimating server tick rates, and integrating optional client configuration GUIs without causing fatal <code>ClassNotFoundException</code> crashes on headless dedicated servers.</p>

<p><strong>Dasik Library</strong> is the universal standalone foundation engineered to solve these challenges. It provides the high-performance architectural backbone powering every mod across the <strong>Vanilla Outsider</strong>, <strong>Delayed Gratification</strong>, and <strong>Instant Gratification</strong> collections. Designed with strict zero-allocation hot paths, Knot ClassLoader isolation, and modular APIs, it equips developers and players with an enterprise-grade framework for modern Minecraft.</p>

<blockquote>
  <p><strong>This is a Standalone Universal Core Library / API.</strong></p>
  <ul>
    <li><strong>Universal Single-JAR Standard:</strong> Dasik Library builds a single universal cross-version JAR (<code>&ge;26.1.2-</code>, currently <strong><code>v1.8.35</code></strong>) using pure SemVer. It seamlessly loads across <strong>Minecraft 26.1.2, 26.2, 26.3</strong>, and future snapshots.</li>
    <li><strong>Both Client &amp; Dedicated Server Ready:</strong> Fully compatible with singleplayer clients and multiplayer dedicated servers.</li>
    <li><strong>Zero Standalone Gameplay Bloat:</strong> Does not add arbitrary gameplay items or world generation on its own. It runs quietly in the background, providing shared AI, genetics, and registry infrastructure for dependent mods.</li>
  </ul>
</blockquote>

<hr>

<h2>✨ Features</h2>

<h3>🧬 Autonomous Animal Genetics &amp; Breeding API</h3>
<ul>
  <li><strong>Persistent Entity Genetics:</strong> Attaches an entity-agnostic genetic data model to living entities via persistent Fabric attachments (<code>dasik-library:genetics</code>), preserving lineage, UUIDs, and generational traits across world saves.</li>
  <li><strong>Triangular Mutation &amp; Outcross Recovery:</strong> Implements natural biological breeding math, including triangular mutation distribution, parent trait averaging, multi-tier inbreeding penalties, and genetic outcross recovery.</li>
  <li><strong>Size-Stats Builder &amp; Linked Attributes:</strong> Easily bind entity physical scale (<code>minecraft:scale</code>, customizable from <code>0.1f</code> to <code>3.0f</code>) to secondary attributes (Max Health, Movement Speed, Attack Damage) via <code>LinkedAttributeRegistry</code> with linear, inverse, or quadratic scaling.</li>
  <li><strong>Genetics-Driven Loot Modification:</strong> Uses <code>GeneticsLootRegistry</code> and loot hooks to dynamically scale drops, meat yields, and resources based on entity size and genetic vitality.</li>
</ul>

<h3>⚙️ Dynamic GameRule Registration &amp; Auto-Translation Engine</h3>
<ul>
  <li><strong>On-Demand Registry Unfreezing:</strong> Safely unfreezes <code>BuiltInRegistries.GAME_RULE</code> at runtime via <code>MappedRegistryAccessor</code>, allowing consumer mods and addons to register custom GameRules without startup race conditions or registry lockouts.</li>
  <li><strong>Automatic Translation Injection:</strong> Dynamically generates human-readable translation keys and injects them directly into vanilla's <code>Language</code> map (<code>dasik$injectDynamicGameRuleTranslations</code>), eliminating missing string placeholders (bold category formatting included).</li>
  <li><strong>World Save Persistence:</strong> Seamlessly intercepts <code>SavedData</code> serialization (<code>GameRuleMapMixin</code>), ensuring dynamic GameRule changes made via <code>/gamerule</code> persist reliably in <code>level.dat</code> and <code>game_rules.dat</code>.</li>
  <li><strong>Automated Orphaned Rule Pruning:</strong> Hooks <code>ServerLifecycleEvents.SERVER_STARTING</code> to detect and cleanly purge orphaned dynamic GameRules left behind when mods are uninstalled, keeping world save files pristine.</li>
  <li><strong>Math Conversion Helpers:</strong> Built-in calculation utilities for percentages, permilles, and deciles (<code>getPct</code>, <code>getChance</code>, <code>getProb</code>, <code>getDecileFloat</code>).</li>
</ul>

<h3>🦅 High-Performance Swarm &amp; Flocking AI (Cached Boids)</h3>
<ul>
  <li><strong><em>O(N)</em> Leader-Follower Pattern:</strong> Eliminates expensive <em>O(N<sup>2</sup>)</em> pairwise distance checks by computing and caching group metrics (<code>FlockState</code>) exclusively on the pack Leader, enabling smooth swarms of 100+ entities at a solid 20 TPS.</li>
  <li><strong>Biomechanical Steering:</strong> Full Alignment, Cohesion, and Separation vector steering calculations for realistic aerial murmurations (<code>AerialFlockingStrategy</code>) and terrestrial pack movement (<code>TerrestrialFlockingStrategy</code>).</li>
  <li><strong>Resilient Following:</strong> Integrated water navigation penalty bypass and catch-up teleportation thresholds to ensure pack members never get lost across chunk boundaries.</li>
</ul>

<h3>📡 The Hive Mind &amp; Decoupled Signal Bus</h3>
<ul>
  <li><strong>Decoupled Social Signals:</strong> High-throughput <code>SignalBus</code> enables mobs to emit and receive alerts, warnings, and mood updates across species without hard-coded class couplings.</li>
  <li><strong>Scope Isolation:</strong> Targeted signal delivery (<code>Scope.SAME_SPECIES</code>, <code>Scope.HERD</code>, <code>Scope.GLOBAL</code>) with safe instance checks.</li>
  <li><strong>Lazy Freeze Architecture:</strong> Solves mod initialization race conditions by allowing modules to register social events during mod init while locking the registry safely at server tick.</li>
</ul>

<h3>🖥️ Optional GUI Integration &amp; Server ClassLoader Safety</h3>
<ul>
  <li><strong>Classloader-Isolated Config Screen Resolver:</strong> <code>GuiHelper</code> dynamically discovers and invokes YetAnotherConfigLib (YACL) and Cloth Config via reflection and Knot ClassLoader isolation.</li>
  <li><strong>100% Dedicated Server Crash Immunity:</strong> Completely prevents client GUI classes from loading on dedicated servers, eliminating server startup crashes.</li>
  <li><strong>Standardized Creator Support:</strong> Provides <code>DasikSupportHelper</code> to embed standardized creator support buttons at index 0 across config screen category pages.</li>
</ul>

<h3>🛡️ Enterprise Stability &amp; Zero-Allocation Hot Paths</h3>
<ul>
  <li><strong>Zero-Dependency Version Guard:</strong> <code>ModVersionGuard</code> verifies runtime Minecraft environment compatibility on startup and displays human-readable diagnostic advice if an incompatible API drop is detected.</li>
  <li><strong>Atomic File Persistence:</strong> <code>ConfigHelper</code> manages JSON read/write operations using temporary file atomic swaps and automatic backup generation.</li>
  <li><strong>Zero-Allocation Rendering:</strong> Zero-byte heap allocation architecture across high-frequency tick and render paths.</li>
</ul>

<hr>

<h2>📊 Quick Reference &amp; Mechanics Matrix</h2>

<table>
  <thead>
    <tr>
      <th>Feature Dimension</th>
      <th>Vanilla / Standard Mod Implementation</th>
      <th>Dasik Library (<code>v1.8.35</code>)</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>GameRule Registration</strong></td>
      <td>Static build-time registration only</td>
      <td><strong>Dynamic runtime unfreeze + auto-translation injection</strong></td>
    </tr>
    <tr>
      <td><strong>Save File Persistence</strong></td>
      <td>Only static vanilla rules saved</td>
      <td><strong>Dynamic <code>SavedData</code> storage in <code>game_rules.dat</code></strong></td>
    </tr>
    <tr>
      <td><strong>Orphaned Rule Handling</strong></td>
      <td>Corrupts saves or persists forever</td>
      <td><strong>Automated startup pruning (<code>pruneOrphanedRules</code>)</strong></td>
    </tr>
    <tr>
      <td><strong>Flocking AI Complexity</strong></td>
      <td><em>O(N<sup>2</sup>)</em> pairwise entity distance loops</td>
      <td><strong><em>O(N)</em> Leader-Cached <code>FlockState</code> computation</strong></td>
    </tr>
    <tr>
      <td><strong>Animal Genetics Engine</strong></td>
      <td>Flat vanilla breed inheritance</td>
      <td><strong>Lineage tracking, mutations, scale linking &amp; loot scaling</strong></td>
    </tr>
    <tr>
      <td><strong>Scale Attribute Range</strong></td>
      <td>Rigid vanilla scaling</td>
      <td><strong>Custom scaling clamp <code>[0.1f, 3.0f]</code> with linked stats</strong></td>
    </tr>
    <tr>
      <td><strong>Optional Config GUI</strong></td>
      <td>Prone to server <code>NoClassDefFoundError</code></td>
      <td><strong>Reflection &amp; Knot isolation (<code>GuiHelper</code>) with 0 server risk</strong></td>
    </tr>
    <tr>
      <td><strong>Version Safeguards</strong></td>
      <td>Obscure Mixin crash traces</td>
      <td><strong><code>ModVersionGuard</code> Knot ClassLoader validation</strong></td>
    </tr>
  </tbody>
</table>

<hr>

<h2>🚀 Developer Quick Start &amp; Implementation Guide</h2>

<p>Integrating Dasik Library into your own Fabric mod takes only a few lines of code:</p>

<h3>1. Gradle Setup</h3>
<p>Add the Modrinth Maven repository and declare the dependency in your <code>build.gradle</code>:</p>

<pre><code>repositories {
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
    }
}

dependencies {
    modImplementation "maven.modrinth:dasik-library:1.8.35"
}</code></pre>

<h3>2. Registering a Dynamic GameRule</h3>
<p>Register a GameRule with automatic English translation and category bolding:</p>

<pre><code>import net.dasik.social.api.gamerule.DynamicGameRuleManager;

// Registers 'my_mod:drop_multiplier' with a default value of 2 under the 'Gameplay' category
DynamicGameRuleManager.registerIntegerRule(
    "my_mod:drop_multiplier",
    2,
    "Drop Multiplier",
    "Gameplay"
);</code></pre>

<h3>3. Binding Animal Scale to Health and Speed</h3>
<p>Link physical size to combat and mobility attributes:</p>

<pre><code>import net.dasik.social.api.genetics.SizeStatsBuilder;
import net.dasik.social.api.genetics.LinkMode;
import net.minecraft.world.entity.ai.attributes.Attributes;

SizeStatsBuilder.create("my_mod:beast_genetics")
    .linkAttribute(Attributes.MAX_HEALTH, LinkMode.PROPORTIONAL, 2.0f)
    .linkAttribute(Attributes.MOVEMENT_SPEED, LinkMode.INVERSE, 0.5f)
    .register();</code></pre>

<h3>4. Dynamic Registry Scanning</h3>
<p>Safely observe and handle new registry entries added by other mods:</p>

<pre><code>import net.dasik.social.api.registry.DynamicRegistryScanner;
import net.minecraft.core.registries.BuiltInRegistries;

DynamicRegistryScanner.subscribe(BuiltInRegistries.ITEM, item -&gt; {
    // Process new modded items dynamically on startup and live reload
});</code></pre>

<hr>

<h2>⚙️ Core Configuration &amp; Save Architecture</h2>

<blockquote><p><strong>💡 Config vs. In-Game GameRules:</strong> The global configuration file only defines default values for newly created worlds. In existing worlds, change settings in-game via the <strong>Edit Game Rules</strong> UI screen or the <code>/gamerule</code> command.</p></blockquote>

<p>Dasik Library manages dynamic GameRule states natively within your world save files:</p>
<ul>
  <li><strong>World Storage:</strong> Stored in <code>&lt;world_save&gt;/data/game_rules.dat</code> and <code>level.dat</code>.</li>
  <li><strong>Zero Residual Clutter:</strong> When you remove a mod that registered dynamic rules, Dasik Library's startup pruning engine detects the absent namespace and unregisters orphaned entries, preventing console spam and bloated save files.</li>
</ul>

<hr>

<h2>📖 In-Depth How-To &amp; Integration Playbook</h2>

<h3>1. For Survival Players &amp; Server Administrators</h3>
<ol>
  <li>Download <code>dasik-library-1.8.35.jar</code> (or latest release).</li>
  <li>Place the JAR directly into your <code>.minecraft/mods</code> folder along with <strong>Fabric API</strong>.</li>
  <li>Launch Minecraft. Dasik Library operates silently and efficiently in the background, providing foundational services for your installed mods.</li>
  <li>When configuring GameRules in-game via the <strong>Edit Game Rules</strong> screen or <code>/gamerule</code>, all rules registered by Dasik-powered mods are automatically translated, organized into categories, and saved directly to the world file.</li>
</ol>

<h3>2. For Modpack Creators</h3>
<ol>
  <li><strong>Universal Multi-Version Support:</strong> You do not need separate builds of Dasik Library for minor Minecraft patches&mdash;one build handles MC 26.1.2, 26.2, and 26.3+.</li>
  <li><strong>Server-Safe:</strong> Include Dasik Library in both client and server modpacks without fear of client GUI crashes.</li>
  <li><strong>Clean World Upgrades:</strong> When updating modpacks and removing deprecated mods, the library automatically purges orphaned GameRules on the next server launch.</li>
</ol>

<h3>3. For Mod Developers</h3>
<ol>
  <li>Declare <code>dasik-library</code> in your <code>fabric.mod.json</code>:
<pre><code>"depends": {
  "fabricloader": "&gt;=0.16.10",
  "minecraft": "&gt;=26.1.2-",
  "fabric-api": "*",
  "dasik-library": "&gt;=1.8.15"
}</code></pre>
  </li>
  <li>Leverage <code>DynamicGameRuleManager</code> to avoid writing repetitive GameRule boilerplate and translation files.</li>
  <li>Use <code>GuiHelper</code> to supply optional YACL or Cloth Config GUIs that open seamlessly via ModMenu while preserving 100% server safety.</li>
</ol>

<hr>

<h2>🧩 Recommended Sister Mods &amp; Powered Projects</h2>

<p>Dasik Library serves as the engine for numerous popular mods. Explore these projects powered directly by the library:</p>

<ul>
  <li>📜 <a href="https://www.curseforge.com/minecraft/mc-mods/collapsible-gamerules"><strong>Collapsible Game Rules</strong></a>: Modernizes the Edit Game Rules screen into collapsible categories with real-time search, leveraging Dasik Library's dynamic metadata.</li>
  <li>🐕 <a href="https://www.curseforge.com/minecraft/mc-mods/better-dogs"><strong>Better Dogs</strong></a>: Complete canine behavioral overhaul featuring personality intelligence, pack murmuration, and vehicle riding powered by Dasik Library's AI flocking engine.</li>
  <li>🌿 <a href="https://www.curseforge.com/minecraft/mc-mods/vo-natural-reproduction"><strong>Natural Reproduction</strong></a>: Autonomous 27-species wildlife breeding and genetics engine built directly on Dasik Animal Genetics.</li>
</ul>

<p><em>Explore the full <a href="https://www.curseforge.com/members/dasikigaijin/projects"><strong>Vanilla Outsider and Instant Gratification Collections</strong></a> for more enhancements.</em></p>

<hr>

<h2>☕ Support</h2>

<p>If you appreciate this library and the mods it powers, consider fueling future updates!</p>

<p align="center">
  <a href="https://ko-fi.com/dasikigaijin/tip"><img src="https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&amp;logo=ko-fi&amp;logoColor=white" alt="Ko-fi"></a>
  <a href="https://sociabuzz.com/dasikigaijin/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge" alt="SocioBuzz"></a>
  <a href="https://saweria.co/DasikIgaijinn"><img src="https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge" alt="Saweria"></a>
</p>

<blockquote><p><strong>🇮🇩 Indonesian Users:</strong> SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!</p></blockquote>

<blockquote><p><strong>Dedicated Server Hosting Partner:</strong><br>Looking for a high-performance server to host your community or play with friends? Check out <strong>BisectHosting</strong> for 1-click modpack installations, automated backups, and 24/7 dedicated customer support. Use promo code <strong><code>Dasik</code></strong> for 25% off your first month!</p></blockquote>

<hr>

<h2>📜 Credits &amp; Modpack Permissions</h2>

<table>
  <thead>
    <tr>
      <th>Property</th>
      <th>Information</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Creator / Author</strong></td>
      <td><strong>Dasik</strong> (Rifaditya)</td>
    </tr>
    <tr>
      <td><strong>Collection</strong></td>
      <td><a href="https://www.curseforge.com/members/dasikigaijin/projects">Universal Core Architecture</a></td>
    </tr>
    <tr>
      <td><strong>License</strong></td>
      <td><a href="https://www.gnu.org/licenses/gpl-3.0.html">GNU General Public License v3.0 (GPLv3)</a></td>
    </tr>
    <tr>
      <td><strong>Source Code</strong></td>
      <td><a href="https://github.com/Rifaditya/MC-DasikLibrary">GitHub - Rifaditya/MC-DasikLibrary</a></td>
    </tr>
    <tr>
      <td><strong>Issue Tracker</strong></td>
      <td><a href="https://github.com/Rifaditya/MC-DasikLibrary/issues">GitHub Issues</a></td>
    </tr>
    <tr>
      <td><strong>Documentation / Wiki</strong></td>
      <td><a href="https://github.com/Rifaditya/MC-DasikLibrary/wiki">GitHub Wiki</a></td>
    </tr>
  </tbody>
</table>

<blockquote>
  <p><strong>📦 Modpack Permissions &amp; Distribution:</strong><br>
  You are fully welcome to include this mod in any modpack on any platform! However, the mod file must be downloaded directly through official distribution channels (<strong>CurseForge</strong> or <strong>Modrinth</strong>). Re-uploading, mirroring, or redistributing the original mod JAR to third-party mirror sites, scraper portals, or unauthorized launchers is strictly prohibited.</p>
  <p><strong>⚖️ License &amp; Fork Guidelines (No Zero-Change Re-uploads):</strong><br>
  This project is open-source under the <strong>GNU GPLv3</strong>. You are fully encouraged to inspect the code, learn from it, and fork the repository to create genuine modifications, substantial feature expansions, or community ports&mdash;provided your project remains open-source under GPLv3 with proper attribution.<br>
  <strong>However, straight 1:1 re-uploads, clone forks with no meaningful functional changes, or re-publishing identical builds under different project names (e.g. to farm downloads or rewards) are strictly forbidden.</strong></p>
</blockquote>

<hr>

<p align="center">
  <strong>Made with ❤️ for the Minecraft community</strong><br>
  <em>Part of the Universal Core Architecture</em>
</p>
