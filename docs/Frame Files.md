# About the Frame Whitelist files
Frame files are placed in ./config/bedcraftbeyond/frames, and two of these files are loaded by default:

* wood.json
* stone.json

These two files aren't limited to those frame types, but those are loaded automatically. This will change to ANY file
in the directory soon enough, but until then, expect those two.

The format is as follows:

```json
{
    "type": "TYPE",
    "entries": [ FRAME_ENTRIES_HERE ]
}
```

Type is a given frame type, the same ones the commands use. (wood and stone)

The frame entry looks like this:

```json
{
    "key": "minecraft:planks",
    "whitelist": [ {Entries} ],
    "blacklist": [ {Entries} ]
}
```

Both the whitelist and blacklists are optional. You can actually leave both out, but that wouldn't do very much, would it?

The files are run in order after the oredictionary scans are run. (If enabled)- So if something is giving you a problem, say,
a mod named 'extraplanks' adding planks under the name 'planks', you could add the following entry:

```json
{
    "key": "extraplanks:planks",
    "blacklist": ["0-15"]
}
```

That will remove all the planks from the whitelist for "extraplanks:planks". Blacklists are run first, then whitelists.

Use the files well.