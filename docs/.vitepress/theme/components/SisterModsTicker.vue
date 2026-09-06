<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'

interface ModProject {
  title: string
  slug: string
  description: string
  icon_url: string
}

// 7-day cache guard parameters
const CACHE_KEY = 'dasik_sister_mods_data'
const CACHE_TIME_KEY = 'dasik_sister_mods_time'
const ONE_WEEK_MS = 7 * 24 * 60 * 60 * 1000 // 7 days in milliseconds

const defaultMods: ModProject[] = [
  {
    title: 'VO: Better Dogs',
    slug: 'vanilla-outsider-better-dogs',
    description: 'Enhances vanilla wolves with unique personalities, genetics & pack hunting.',
    icon_url: 'https://cdn.modrinth.com/data/u8E6EsqM/icon.png'
  },
  {
    title: 'DG: Natural Reproduction',
    slug: 'dg-natural-reproduction',
    description: 'Allows all passive mobs to autonomously reproduce with dynamic genetics.',
    icon_url: 'https://cdn.modrinth.com/data/L5sJbX2e/icon.png'
  },
  {
    title: 'Collapsible gamerules screen',
    slug: 'collapsible-gamerules',
    description: 'Makes the GameRules UI screen collapsible by category with instant search.',
    icon_url: 'https://cdn.modrinth.com/data/bH9N0T7o/icon.png'
  },
  {
    title: 'IG: Ore Amplifier',
    slug: 'instant-gratification-ore-amplifier',
    description: 'Multiply ore vein frequency and yields by up to 50x.',
    icon_url: 'https://cdn.modrinth.com/data/Gv4Z5jEa/icon.png'
  },
  {
    title: 'VO: Better Bats',
    slug: 'vo-better-bats',
    description: 'Living cave ecosystems, echolocation mechanics, and dynamic behaviors.',
    icon_url: 'https://cdn.modrinth.com/data/j3LqP09z/icon.png'
  },
  {
    title: 'IG: Item Clumps',
    slug: 'ig-item-clumps',
    description: 'Redesigns dropped item physics and grouping for zero lag.',
    icon_url: 'https://cdn.modrinth.com/data/YyqL92M7/icon.png'
  },
  {
    title: 'IG: Magnet, Let me get that!',
    slug: 'instant-gratification-magnet,-let-me-get-that!',
    description: 'Intrinsic item vacuum for the modern player.',
    icon_url: 'https://cdn.modrinth.com/data/vj3B8X4z/icon.png'
  },
  {
    title: 'IG: Durability Multiplier',
    slug: 'instant-gratification-durability-multiplier',
    description: 'Complete configurable authority over tool and armor durability.',
    icon_url: 'https://cdn.modrinth.com/data/8KxN2Y1m/icon.png'
  },
  {
    title: 'IG: Max Elytra Fly Speed',
    slug: 'ig-max-elytra-fly-speed',
    description: 'Bypass vanilla kinetic flight barriers at maximum velocity.',
    icon_url: 'https://cdn.modrinth.com/data/z59BvW1r/icon.png'
  },
  {
    title: 'VO: True Sleep',
    slug: 'vanilla-outsider-true-sleep',
    description: 'Smooth simulated night time acceleration instead of skipping.',
    icon_url: 'https://cdn.modrinth.com/data/m9X2Q1pA/icon.png'
  },
  {
    title: 'VO: Bed Chat Hider',
    slug: 'vo-bed-chat-hider',
    description: 'Client-only quality of life mod hiding sleep spam.',
    icon_url: 'https://cdn.modrinth.com/data/3K9xL2pQ/icon.png'
  },
  {
    title: 'IG: Stack Size Adjuster',
    slug: 'ig-stack-size-adjuster',
    description: 'Full authority over item stack limits in inventory and chests.',
    icon_url: 'https://cdn.modrinth.com/data/q7Xm2P8z/icon.png'
  }
]

const mods = ref<ModProject[]>(defaultMods)

onMounted(async () => {
  try {
    const cachedData = localStorage.getItem(CACHE_KEY)
    const cachedTime = localStorage.getItem(CACHE_TIME_KEY)
    const now = Date.now()

    // 1. If cache is fresh (< 7 days old), use it directly and NEVER make any network call
    if (cachedData && cachedTime && (now - Number(cachedTime) < ONE_WEEK_MS)) {
      mods.value = JSON.parse(cachedData)
      return
    }

    // 2. Only fetch once every 7 days
    const res = await fetch('https://api.modrinth.com/v2/user/dasikigaijin/projects')
    if (res.ok) {
      const data = await res.json()
      const filtered = data
        .filter((p: any) => p.slug !== 'dasik-library')
        .map((p: any) => ({
          title: p.title,
          slug: p.slug,
          description: p.description,
          icon_url: p.icon_url || ''
        }))

      if (filtered.length > 0) {
        mods.value = filtered
        localStorage.setItem(CACHE_KEY, JSON.stringify(filtered))
        localStorage.setItem(CACHE_TIME_KEY, String(now))
      }
    }
  } catch (err) {
    // If offline or error, smoothly retain default fallback
  }
})

const row1 = computed(() => {
  const half = Math.ceil(mods.value.length / 2)
  const items = mods.value.slice(0, half)
  return [...items, ...items]
})

const row2 = computed(() => {
  const half = Math.ceil(mods.value.length / 2)
  const items = mods.value.slice(half)
  return [...items, ...items]
})
</script>

<template>
  <section class="sister-mods-wrapper">
    <div class="section-header">
      <span class="pill-tag">Ecosystem</span>
      <h3 class="section-title">Powered by Dasik</h3>
      <p class="section-subtitle">Explore sister mods and companion tools built on this foundation</p>
    </div>

    <div class="marquee-container">
      <!-- Row 1 (Drifts Left) -->
      <div class="marquee-row">
        <div class="marquee-track scroll-left">
          <a
            v-for="(mod, idx) in row1"
            :key="'r1-' + idx"
            :href="'https://modrinth.com/mod/' + mod.slug"
            target="_blank"
            rel="noopener noreferrer"
            class="mod-card"
          >
            <div class="mod-icon-wrapper">
              <img
                v-if="mod.icon_url"
                :src="mod.icon_url"
                :alt="mod.title"
                class="mod-icon"
                loading="lazy"
              />
              <div v-else class="mod-icon-placeholder">🧩</div>
            </div>
            <div class="mod-info">
              <span class="mod-title">{{ mod.title }}</span>
              <span class="mod-desc">{{ mod.description }}</span>
            </div>
          </a>
        </div>
      </div>

      <!-- Row 2 (Drifts Right) -->
      <div class="marquee-row">
        <div class="marquee-track scroll-right">
          <a
            v-for="(mod, idx) in row2"
            :key="'r2-' + idx"
            :href="'https://modrinth.com/mod/' + mod.slug"
            target="_blank"
            rel="noopener noreferrer"
            class="mod-card"
          >
            <div class="mod-icon-wrapper">
              <img
                v-if="mod.icon_url"
                :src="mod.icon_url"
                :alt="mod.title"
                class="mod-icon"
                loading="lazy"
              />
              <div v-else class="mod-icon-placeholder">🧩</div>
            </div>
            <div class="mod-info">
              <span class="mod-title">{{ mod.title }}</span>
              <span class="mod-desc">{{ mod.description }}</span>
            </div>
          </a>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.sister-mods-wrapper {
  margin: 40px auto 20px;
  max-width: 1152px;
  padding: 0 24px;
}

.section-header {
  text-align: center;
  margin-bottom: 24px;
}

.pill-tag {
  display: inline-block;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  padding: 3px 10px;
  border-radius: 9999px;
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  margin-bottom: 8px;
}

.section-title {
  font-size: 24px;
  font-weight: 700;
  letter-spacing: -0.02em;
  color: var(--vp-c-text-1);
  margin: 0;
}

.section-subtitle {
  font-size: 14px;
  color: var(--vp-c-text-2);
  margin-top: 6px;
}

.marquee-container {
  position: relative;
  overflow: hidden;
  padding: 8px 0;
  mask-image: linear-gradient(to right, transparent, black 8%, black 92%, transparent);
  -webkit-mask-image: linear-gradient(to right, transparent, black 8%, black 92%, transparent);
}

.marquee-row {
  display: flex;
  overflow: hidden;
  margin-bottom: 12px;
}

.marquee-row:last-child {
  margin-bottom: 0;
}

.marquee-track {
  display: flex;
  gap: 14px;
  width: max-content;
  will-change: transform;
}

.scroll-left {
  animation: scrollLeft 42s linear infinite;
}

.scroll-right {
  animation: scrollRight 46s linear infinite;
}

.marquee-row:hover .marquee-track {
  animation-play-state: paused;
}

@keyframes scrollLeft {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
  }
}

@keyframes scrollRight {
  0% {
    transform: translateX(-50%);
  }
  100% {
    transform: translateX(0);
  }
}

.mod-card {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 290px;
  height: 66px;
  padding: 10px 14px;
  border-radius: 12px;
  background: var(--vp-c-bg-soft);
  border: 1px solid var(--vp-c-default-soft);
  text-decoration: none !important;
  color: inherit;
  flex-shrink: 0;
  transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
}

.mod-card:hover {
  border-color: var(--vp-c-brand-1);
  transform: translateY(-2px);
  background: var(--vp-c-bg-elv);
}

.dark .mod-card:hover {
  box-shadow: 0 8px 20px -6px rgba(255, 255, 255, 0.08);
}

:root:not(.dark) .mod-card:hover {
  box-shadow: 0 8px 20px -6px rgba(0, 0, 0, 0.12);
}

.mod-icon-wrapper {
  width: 42px;
  height: 42px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
  background: var(--vp-c-default-soft);
  display: flex;
  align-items: center;
  justify-content: center;
}

.mod-icon {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.mod-icon-placeholder {
  font-size: 20px;
}

.mod-info {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  justify-content: center;
}

.mod-title {
  font-size: 13.5px;
  font-weight: 600;
  color: var(--vp-c-text-1);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mod-desc {
  font-size: 11.5px;
  color: var(--vp-c-text-2);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-top: 2px;
}

@media (prefers-reduced-motion: reduce) {
  .scroll-left,
  .scroll-right {
    animation: none;
  }
}
</style>
