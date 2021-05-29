## Pipes
How to: Pipes

### Table of Contents

* PipeItem
* PipeTile
* PipeBlock
* PipeNetworks  
* WorldCapability
* Ticking
* Adding/deleting networks
* Appending to/removing from networks
* merging/Splitting networks

### PipeItem

The three PipeItems are BlockItems which all point to the MultiPipe Block.  
BlockItems only show their block.asItem item in ItemGroups/Search. To prevent displaying the itemPipe 3 times, the method fillItemGroup is overridden and replaced with the normal Item Implementation.  

### PipeTile

The PipeTile has a Map translating PipeTypes to NetworkUUIDs. The UUIDs are used to identify networks without reference for easy usage through serialization/deserialization.  
The onLoad Method is called, whenever a TilEntity is loaded. It's used to update TileEntity even when they are unloaded. For further information See Merging/Splitting networks.

### PipeBlock

The PipeBlock manages access to PipeTile and placement of multiple pipes into one BlockState.  
The onBlockPlaced method and onBlockActivated method call a method which will handle the addition of a pipe to a block. More on that in the last 3 sections of this document.  
The onReplaced method call a method with will handle the removing of pipes. More on that in the last 3 sections of this document.

### PipeNetworks

PipeNetworks handle the complete ticking action of the networks. They store the connectedBlocks the inputs and the outputs. More on the Action is described in the Ticking section of the document.  
They are abstract on all sections, that need to be used to interact with capabilities to transport. This is the reason, so that no one need to know, how and what to transport. This makes handling the networks much easier.

### WorldCapability

The WorldCapability is the Storage of all PipeNetworks in a Map alongside their UUIDs. They are added to each ServerWorld and can be serialized and deserialized for storage reasons, so that the pipes stay intact even after ServerShutdown.
The WorldCapability also provides the necessary methods for the placing and breaking of pipeBlocks. More on that in the last 3 Sections of this document.

### Ticking

The WorldTickEvent is called for each world. For every ServerWorld we tick the WorldCapability to notify it, that a tick has happened.  
The WorldCapability notifies every network, that a tick has happened.  
The PipeNetwork first checks, if it should be removed from the worldCapability. This is the case, if it is empty or if it is marked as deprecated and it doesn't have any new NetworkUUIDs to point a PipeTile to. This ensures that no unused networks are there and prevent a memory leak.
The PipeNetwork then goes over every input tries to drain from the first Slot(or tank if it is a fluidNetwork or first nothing, if it is an energyNetwork). The Inputfilter then check, if the item can be extracted and if it can't the next slot is drained.  
If it can be extracted the PipeNetwork tries to find the first Storage to fill the transported thing. The transported thing then gets removed from the storage. If not matching output can be found, the next Slot is tested.
After it can find an output, the Input stops trying, and the next input is drained.

### Adding/Deleting Networks
These 3 sections are represented in the flowchart I showed you earlier.(if someone on github reads this: You can ask me (agnor99#5489 on discord for a copy of that image))  
The simplest operation on a network. If a pipe is next to nothing and broken, delete all associated networkData.  
If a pipe is placed and no other pipe of that type is around, a new network is created, and the pipe is added to the network.

### Appending/Removing Network
Another simple operation on a network. If a placed pipe is surrounded by only 1 Pipe Network of the same type (It can be multiple pipes with the same network UUID) The current pipe is added to the network.
If a broken pipe is connected to 1 other pipe(No networks, real pipes) the pipe is removed from the network.

### Merging/Splitting Networks
This is the complicated part:  
If a placed pipe is around multiple distinct UUIDs a new network is created and all the blocks, inputs and outputs are put into the new network. All PipeTileEntities belonging to the old networks get the new networkID inserted into the type. If a chunk is unloaded, the new NetworkUUID will be put into a Map with the BlockPos as the key. On TileLoad the pipeTile asks for the new network UUID if one is present. The Entry in the newUUID-Map is removed and if it is empty, the network will get removed from the capability.  
If a removed pipe is around multiple pipes a network rebuild is created. The Network rebuild happens like in the following graphic (I know it's not the fastest algorithm, build one if you want to):  
![splitting of networks](splitting%20of%20networks.png)  
A map with the direction of each initial search point with the connected BlockPositions is returned to find out, which blocks belong to which networks.
if only one network is returned, all blocks belong to the same network, so no new Network is created with UUIDs.
If multiple Lists of connected blocks are returned, new networks are created and all the data is copied to the new networks from the old network. Which BlockPos belongs to which new Network is also put in a map, to deprecate the old network in the same way as described above.
