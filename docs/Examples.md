# Frame File Examples

Here's an example to add Forestry, Biomes o Plenty, and Vanilla support to wood frames.
It does not add support for fireproof planks and doesn't plop tons of invalid oak wood frames
into the registry. If you need a good starting point, here ya go:

```json
{
  "entries": [
    {
      "key": "minecraft:planks",
      "type": "wood",
      "whitelist": ["0-5"]
    },

    {
      "key": "biomesoplenty:planks_0",
      "type": "wood",
      "whitelist": ["0-15"]
    },

    {
      "key": "forestry:planks.0",
      "type": "wood",
      "whitelist": ["0-15"]
    },
    
    {
      "key": "forestry:planks.1",
      "type": "wood",
      "whitelist": ["0-12"]
    }
  ]
}
```
