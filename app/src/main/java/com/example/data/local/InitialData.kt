package com.example.data.local

import com.example.data.model.ComponentItem

object InitialData {
  fun getPresetComponents(): List<ComponentItem> = listOf(
    ComponentItem(
      title = "AMD Ryzen 9 9800X3D Processor",
      categoryBadge = "CPU / Desktop Processor",
      overview = "The AMD Ryzen 9 9800X3D is an elite gaming processor utilizing 2nd Generation AMD 3D V-Cache technology. It features Zen 5 architecture cores designed to deliver maximum frame rates and efficiency for high-end gaming rigs and workstations.",
      specsRaw = "Architecture: Zen 5 (Granite Ridge), Cores / Threads: 8 Cores / 16 Threads, L3 Cache: 96 MB (3D V-Cache), Base / Boost Clock: 4.7 GHz / 5.2 GHz, TDP: 120 W, Socket Support: AM5",
      datasheetUrl = "https://www.amd.com/en/products/processors/desktops/ryzen/9000-series/amd-ryzen-9-9800x3d.html",
      isFavorite = true,
      timestamp = System.currentTimeMillis() - 100000,
      isUserCreated = false
    ),
    ComponentItem(
      title = "Intel Core i7-13700K Processor",
      categoryBadge = "CPU / Integrated Circuit",
      overview = "The Intel Core i7-13700K is a high-performance desktop processor based on the Raptor Lake architecture, combining Performance cores and Efficient cores for heavy multi-threaded workloads.",
      specsRaw = "Architecture: Raptor Lake (13th Gen), Total Cores / Threads: 16 Cores (8P + 8E) / 24 Threads, Base / Max Clock Speed: 3.40 GHz / 5.40 GHz, Base Power (TDP): 125 W (Max Turbo 253 W), Socket Support: LGA1700",
      datasheetUrl = "https://www.intel.com/content/www/us/en/products/sku/230490/intel-core-i713700k-processor.html",
      isFavorite = false,
      timestamp = System.currentTimeMillis() - 200000,
      isUserCreated = false
    ),
    ComponentItem(
      title = "Apple M2 System-on-Chip (SoC)",
      categoryBadge = "SoC / Custom Silicon",
      overview = "The Apple M2 chip represents the second generation of Apple’s M-series silicon, offering industry-leading performance-per-watt for MacBook Air, MacBook Pro, and iPad models.",
      specsRaw = "Nodes: 5nm (Second Generation), CPU Cores: 8-core (4 performance, 4 efficiency), GPU Cores: Up to 10-core GPU, Unified Memory: Up to 24 GB, Memory Bandwidth: 100 GB/s",
      datasheetUrl = "https://support.apple.com/en-us/111867",
      isFavorite = true,
      timestamp = System.currentTimeMillis() - 300000,
      isUserCreated = false
    ),
    ComponentItem(
      title = "NVIDIA GeForce RTX 5090",
      categoryBadge = "GPU / Desktop Graphics",
      overview = "Flagship consumer graphics processing unit built on NVIDIA Blackwell architecture, engineered for extreme AI computing, real-time path tracing, and high-fidelity 4K/8K rendering.",
      specsRaw = "Architecture: Blackwell, CUDA Cores: 21760, VRAM: 32 GB GDDR7, Memory Bus: 512-bit, Memory Bandwidth: 1792 GB/s, TDP: 600 W",
      datasheetUrl = "https://www.nvidia.com/en-us/geforce/graphics-cards/50-series/",
      isFavorite = false,
      timestamp = System.currentTimeMillis() - 400000,
      isUserCreated = false
    ),
    ComponentItem(
      title = "Qualcomm Snapdragon 8 Elite",
      categoryBadge = "SoC / Mobile Processor",
      overview = "Next-generation mobile platform featuring Qualcomm Oryon custom CPU architecture designed to deliver unparalleled computing power and on-device generative AI acceleration.",
      specsRaw = "Architecture: Oryon 2nd Gen 64-bit, Fabrication: 3nm TSMC, CPU Config: 2x Prime 4.32 GHz + 6x Perf 3.53 GHz, GPU: Adreno 830, NPU: Hexagon AI Engine",
      datasheetUrl = "https://www.qualcomm.com/products/mobile/snapdragon/smartphones/snapdragon-8-series-mobile-platforms/snapdragon-8-elite-mobile-platform",
      isFavorite = false,
      timestamp = System.currentTimeMillis() - 500000,
      isUserCreated = false
    ),
    ComponentItem(
      title = "Raspberry Pi RP2350 Microcontroller",
      categoryBadge = "IC / Microcontroller",
      overview = "High-performance, secure microcontroller designed by Raspberry Pi, offering dual Arm Cortex-M33 and dual Hazard3 RISC-V cores with comprehensive hardware security.",
      specsRaw = "Cores: Dual Cortex-M33 or Dual RISC-V @ 150 MHz, SRAM: 520 KB, Flash: External QSPI (up to 16MB), Security: Arm TrustZone & OTP, GPIO: 30x Multi-function",
      datasheetUrl = "https://datasheets.raspberrypi.com/rp2350/rp2350-datasheet.pdf",
      isFavorite = false,
      timestamp = System.currentTimeMillis() - 600000,
      isUserCreated = false
    ),
    ComponentItem(
      title = "Samsung 990 PRO NVMe M.2 SSD",
      categoryBadge = "Storage / PCIe SSD",
      overview = "Top-tier PCIe 4.0 NVMe solid state drive engineered for tech enthusiasts and heavy gamers seeking blisteringly fast sequential read/write speeds and low latency.",
      specsRaw = "Interface: PCIe Gen 4.0 x4 NVMe 2.0, Sequential Read: Up to 7450 MB/s, Sequential Write: Up to 6900 MB/s, Controller: Samsung Pascal, DRAM: Low Power DDR4",
      datasheetUrl = "https://semiconductor.samsung.com/consumer-storage/internal-ssd/990-pro/",
      isFavorite = false,
      timestamp = System.currentTimeMillis() - 700000,
      isUserCreated = false
    )
  )
}
