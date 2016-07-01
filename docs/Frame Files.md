# About the Frame Whitelist files
Frame files are placed in ./config/bedcraftbeyond/frames, and anything in this directory is loaded.

The format is as follows:

```json
{
    "entries": [ FRAME_ENTRIES_HERE ]
}
```

The frame entry looks like this:

```json
{
    "key": "minecraft:planks",
    "type": "TYPE",
    "whitelist": [ {Entries} ],
    "blacklist": [ {Entries} ]
}
```

TYPE is any of the frame types. These are 'wood' or 'stone', at the moment.

Both the whitelist and blacklists are optional. You can actually leave both out, but that wouldn't do very much, would it?

The files are run in order after the oredictionary scans are run. (If enabled)- So if something is giving you a problem, say,
a mod named 'extraplanks' adding planks under the name 'planks', you could add the following entry:

```json
{
    "key": "extraplanks:planks",
    "type": "wood",
    "blacklist": ["0-15"]
}
```

That will remove all the planks from the whitelist for "extraplanks:planks". Blacklists are run first, then whitelists.

Use the files well. They can be pretty powerful.
