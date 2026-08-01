<div align="center">

<img src="https://media.forgecdn.net/attachments/1513/813/dasik-page-banner-jpg.jpg" alt="Dasik Library Banner">

</div>
<p align="center">
    <a href="https://www.curseforge.com/minecraft/mc-mods/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-LGPLv3-green?style=for-the-badge" alt="License">
</p>

<h1>🧠 Dasik Library</h1>

<p><strong>Active Version Target:</strong> Supported on <strong>Minecraft 26.1.2+</strong> (including 26.2+). Older versions are unsupported.</p>

<blockquote>
    <strong>This is a Core Library / API.</strong><br>
    It provides shared functionality for my mods (Vanilla Outsider, Instant Gratification, and Delayed Gratification collections). It does nothing on its own.
</blockquote>

<p><strong>Dasik Library</strong> provides a unified <strong>Hive Mind</strong> architecture for complex social behaviors. Entities become aware of "Social Events" (Alerts, Moods, Requests), dynamic genetics, flocking algorithms, and participate in a global, optimized social scheduler.</p>

<hr>

<h2>✨ Features</h2>

<h3>📡 The Hive Mind (Global Signal System)</h3>

<p>Entities don't just "tick"—they <strong>pulse</strong>.</p>

<ul>
    <li><strong>Unified Scheduler:</strong> One centralized system manages social interactions for all supported mods.</li>
    <li><strong>Performance Optimized:</strong> Uses a "Highlander" pattern ensuring logic only runs once per tick, even if multiple mods consume the library.</li>
</ul>

<h3>🎭 Social Events Registry</h3>

<p>A standardized way for entities to communicate across mods.</p>

<ul>
    <li><strong>Tracks:</strong> Events are categorized into priority tracks (e.g., <code>Mood</code> for high priority, <code>Ambient</code> for low priority).</li>
    <li><strong>Lazy Freeze:</strong> Respects mod loading order while locking down tight for world save stability once the server initializes.</li>
</ul>

<blockquote>
    <strong>Developer Note:</strong><br>
    This library solves the "Mod Initialization Race Condition" using the <strong>Lazy Freeze Pattern</strong>. You can register events safely in your <code>onInitialize</code>, and the system won't lock until <code>ServerTick</code>.
</blockquote>

<h3>🤝 Inter-Mod Compatibility</h3>

<ul>
    <li><strong>Better Dogs:</strong> Handles pack dynamics, howling signals, and wolf leader-follower flocking.</li>
    <li><strong>MCA Female Gender Bridge:</strong> Integrates genetics &amp; breeding physics solvers.</li>
    <li><strong>Collapsible Game Rule Screen:</strong> Powers dynamic GameRule registration and localization helpers.</li>
</ul>

<h3>🐺 Leader-Follower API (Cached Boids)</h3>

<p>A high-performance flocking API in the Fabric ecosystem.</p>
<ul>
    <li><strong>Cached Boids Pattern:</strong> Supports swarms (100+ entities) with minimal CPU load by pre-calculating group data on the Leader.</li>
    <li><strong>Biomechanical Steering:</strong> Alignment, Cohesion, and Separation steering for organic pack movement.</li>
    <li><strong>Entity Scaling &amp; Genetics:</strong> Mapped directly to vanilla scale attributes and dynamic GameRule limits.</li>
</ul>

<h3>🧬 Genetics &amp; Breeding Engine</h3>

<p>Entity-agnostic genetics attachment, triangular mutation rules, inbreeding penalties, and linked attribute scaling (health, speed, damage).</p>

<hr>

<h2>📦 Installation</h2>

<ol>
    <li>Install <strong><a href="https://www.curseforge.com/minecraft/mc-mods/fabric-api">Fabric API</a></strong>.</li>
    <li>Download the latest version of <strong>Dasik Library</strong> matching your Minecraft release and place it in your <code>mods</code> folder.</li>
    <li><em>Required by all Vanilla Outsider, Instant Gratification, and Delayed Gratification mods.</em></li>
</ol>

<hr>

<h2>☕ Support</h2>

<p>If you enjoy the ecosystem, consider fueling the next update!</p>

<p align="center">
    <a href="https://ko-fi.com/dasikigaijin/tip"><img src="https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Ko-fi"></a>
    <a href="https://sociabuzz.com/dasikigaijin/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge" alt="SocioBuzz"></a>
    <a href="https://saweria.co/DasikIgaijinn"><img src="https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge" alt="Saweria"></a>
</p>

<blockquote>
    <strong>Indonesian Users:</strong> SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!
</blockquote>

<hr>

<h2>📜 Credits</h2>

<table>
    <thead>
        <tr>
            <th>Role</th>
            <th>Author</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td><strong>Creator</strong></td>
            <td><strong>Dasik (Rifaditya)</strong></td>
        </tr>
        <tr>
            <td><strong>Collection</strong></td>
            <td>Vanilla Outsider / Instant Gratification / Delayed Gratification</td>
        </tr>
        <tr>
            <td><strong>License</strong></td>
            <td>LGPLv3</td>
        </tr>
    </tbody>
</table>

<hr>

<blockquote>
    <strong>📦 Modpack Permissions:</strong><br>
    You are free to include this mod in any modpack on any platform. However, the mod itself must be downloaded from its official distribution pages on <strong>Modrinth</strong> or <strong>CurseForge</strong>. Re-uploading or redistributing the mod jar file to third-party sites is strictly prohibited unless explicitly permitted by the creator.
</blockquote>

<hr>

<div align="center">

<p><strong>Made with ❤️ for the Minecraft community</strong><br>
<em>Part of the Vanilla Outsider Collection</em></p>

</div>
