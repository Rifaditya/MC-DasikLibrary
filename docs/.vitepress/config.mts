import { defineConfig } from 'vitepress'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  title: 'Dasik Library',
  description: 'High-performance foundational library for modern Minecraft Fabric mods.',
  head: [
    ['link', { rel: 'icon', href: '/favicon.ico' }],
    ['meta', { name: 'theme-color', content: '#333333' }]
  ],
  vite: {
    publicDir: fileURLToPath(new URL('../../Images', import.meta.url))
  },
  themeConfig: {
    siteTitle: 'Dasik Library',
    logo: {
      src: '/brand-logo.webp',
      alt: 'Dasik Brand'
    },
    nav: [
      { text: 'Documentation', link: '/guides/getting-started' },
      { text: 'Architecture', link: '/guides/Architecture-and-Package-Layout' },
      { text: 'GitHub', link: 'https://github.com/Rifaditya/MC-DasikLibrary' }
    ],
    sidebar: [
      {
        text: 'Overview & Setup',
        items: [
          { text: 'Getting Started', link: '/guides/getting-started' },
          { text: 'Architecture & Layout', link: '/guides/Architecture-and-Package-Layout' },
          { text: 'Version Compatibility', link: '/guides/Version-Compatibility' },
          { text: 'Minecraft 26.2 Guide', link: '/guides/Minecraft-26.2-Guide' },
          { text: 'Developer Setup & Building', link: '/guides/Developer-Setup-and-Building' }
        ]
      },
      {
        text: 'Genetics & Entities',
        items: [
          { text: 'Animal Genetics Engine', link: '/guides/Animal-Genetics-Engine' },
          { text: 'Genetics API & Pedigree', link: '/guides/Genetics-API-and-Pedigree' },
          { text: 'Genetics Loot Modifiers', link: '/guides/Genetics-Loot-Modifiers' },
          { text: 'Stale Attribute Purging', link: '/guides/Stale-Attribute-Purging-and-Scale' }
        ]
      },
      {
        text: 'Social & AI Systems',
        items: [
          { text: 'Hive Mind Social System', link: '/guides/Hive-Mind-Social-System' },
          { text: 'Social Scheduler & Events', link: '/guides/Social-Scheduler-and-Events' },
          { text: 'Leader Follower & Flocking', link: '/guides/Leader-Follower-and-Flocking' },
          { text: 'Behavior Profiles & Conditions', link: '/guides/Behavior-Profiles-and-Conditions' }
        ]
      },
      {
        text: 'GameRules & Engine Core',
        items: [
          { text: 'Dynamic GameRules Manager', link: '/guides/Dynamic-GameRules-Manager' },
          { text: 'GameRule Codec & Serialization', link: '/guides/GameRule-Codec-and-Serialization' },
          { text: 'Dynamic Enchantments & Vision', link: '/guides/Dynamic-Enchantments-and-Vision' },
          { text: 'Stochastic & Math Utilities', link: '/guides/Stochastic-and-Math-Utilities' },
          { text: 'ModVersionGuard & Startup Safety', link: '/guides/ModVersionGuard-and-Startup-Safety' }
        ]
      },
      {
        text: 'Integration & Reference',
        items: [
          { text: 'Consumer Mods Integration', link: '/guides/Consumer-Mods-Integration-Guide' },
          { text: 'Client GameRule & GUI Helpers', link: '/guides/Client-GameRule-and-GUI-Helpers' },
          { text: 'Mixin Reference & Hooks', link: '/guides/Mixin-Reference-and-Hooks' }
        ]
      }
    ],
    search: {
      provider: 'local'
    },
    socialLinks: [
      { icon: 'github', link: 'https://github.com/Rifaditya/MC-DasikLibrary' }
    ],
    footer: {
      message: 'Released under GNU GPLv3 License.',
      copyright: 'Copyright © 2026 Dasik (Rifaditya)'
    }
  }
})
