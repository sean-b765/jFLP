Parse FLP project files
A project to help organise my massive flp projects folder

With help from: 
https://github.com/andrewrk/PyDaw/blob/master/FLP_Format
```
How it works:

Don't expect the FLP format to be a big chunk full of ordered parameters, like
most trackers file formats. It had to evolute, so I chose the 'events' way.
You should see it a bit like MIDI / AIFF files. It's just a succession of
events. Once you've understand how to process the file to retrieve these
events, the only thing you'll need is the list of events available!

It's better, since once you've made the piece of code to get the events, you
won't bother with the format anymore. Also you will just ignore any event you
don't know (yet) about.

Please note that the format *does not* respect the AIFF standard, although I
tried to keep the chunks system similar.

Although the program has been coded in Pascal, I'll do my best to use C++
declarations, so everyone will understand. DWORD is 4 bytes, WORD is 2 bytes.




Retrieving the events:

I said it looks like a MIDI file, but it's not a MIDI file.

First, you'll have to get & check the HEADER chunk, to be sure it's a FLP file.
The header is similar to the format of a MIDI file header:

DWORD	ChunkID	4 chars which are the letters 'FLhd' for 'FruityLoops header'
DWORD	Length	The length of this chunk, like in MIDI files. Should be 6
because of the 3 WORDS below...  WORD	Format	Set to 0 for full songs.  WORD
nChannels	The total number of channels (not really used).  WORD	BeatDiv
Pulses per quarter of the song.

Most of this chunk is not used, it's just that I tried (as a start) to respect
the proper MIDI header :)


Then you'll encounter the DATA chunk, which is in fact the last chunk, the one
containing all the events.

DWORD	ChunkID	4 chars which are the letters 'FLdt' for 'FruityLoops data'
DWORD	Length	The length of this chunk WITHOUT these 2 DWORDS (that is minus
4*2 bytes), like in MIDI files.


The whole data chunk is a succession of EVENTS, which I'm going to explain...
To retrieve an event, first you read a byte (the event ID). According to this
byte, the size of the event data varies: 0..63	The data after this byte is a
BYTE (signed or unsigned, depending on the ID).  64..127	The data after this
byte is a WORD.  128..192	The data after this byte is a DWORD.  192..255	The
data after this byte is a variable-length block of data (a text for example).

That makes 64 BYTE events, 64 WORD events, 64 DWORD events & 64 TEXT events.
The purpose of this split is of course to keep the file size small.  So you get
the event ID & then you read the number of bytes according to this ID. Whether
you process the event or not isn't important. What is important is that you can
jump correctly to the next event if you skip it.


For TEXT (variable-length) events, you still have to read the size of the
event, which is coded in the next byte(s) a bit like in MIDI files (but not
stupidly inverted). After the size is the actual data, which you can process or
skip.  To get the size of the event, you've got to read bytes until the last
one, which has bit 7 off (the purpose of this compression is to reduce the file
size again).

Start with a DWORD Size = 0. You're going to reconstruct the size by getting
packs of 7 bits: 1.	Get a byte.  2.	Add the first 7 bits of this byte to Size.
3.	Check bit 7 (the last bit) of this byte. If it's on, go back to 1. to
      process the next byte.

To resume, if Size < 128 then it will occupy only 1 byte, else if Size < 16384
it will occupy only 2 bytes & so on...


So globally, you open the file, check the header, point to the data chunk &
retrieve / filter all the events. Easy(?) Now let's get to the events...




The events list:

Some events are obvious. Some others will have to be explained in details. But
to understand how to process the file, you've got to know how the FLP is stored
because the order of most of these events *IS* important.  For example, when
storing a channel, an FLP_NewChan event is added. Then any next 'channel' event
will affect this newly created channel.  To spare some space, some events are
stored only if they differ from their default value.


// BYTE EVENTS
FLP_Byte      	=0;
FLP_Enabled   	=0;
FLP_NoteOn    	=1;  	//+pos (byte)
FLP_Vol       	=2;
FLP_Pan       	=3;
FLP_MIDIChan  	=4;
FLP_MIDINote  	=5;
FLP_MIDIPatch 	=6;
FLP_MIDIBank  	=7;
FLP_LoopActive	=9;
FLP_ShowInfo  	=10;
FLP_Shuffle   	=11;
FLP_MainVol   	=12;
FLP_Stretch   	=13; 	// old byte version
FLP_Pitchable 	=14;
FLP_Zipped    	=15;
FLP_Delay_Flags	=16;
FLP_PatLength 	=17;
FLP_BlockLength	=18;
FLP_UseLoopPoints	=19;
FLP_LoopType  	=20;
FLP_ChanType  	=21;
FLP_MixSliceNum	=22;

// WORD EVENTS
FLP_Word     	=64;
FLP_NewChan  	=FLP_Word;
FLP_NewPat   	=FLP_Word+1;  	//+PatNum (word)
FLP_Tempo    	=FLP_Word+2;
FLP_CurrentPatNum	=FLP_Word+3;
FLP_PatData  	=FLP_Word+4;
FLP_FX       	=FLP_Word+5;
FLP_Fade_Stereo	=FLP_Word+6;
FLP_CutOff   	=FLP_Word+7;
FLP_DotVol   	=FLP_Word+8;
FLP_DotPan   	=FLP_Word+9;
FLP_PreAmp   	=FLP_Word+10;
FLP_Decay    	=FLP_Word+11;
FLP_Attack   	=FLP_Word+12;
FLP_DotNote  	=FLP_Word+13;
FLP_DotPitch 	=FLP_Word+14;
FLP_DotMix   	=FLP_Word+15;
FLP_MainPitch	=FLP_Word+16;
FLP_RandChan 	=FLP_Word+17;
FLP_MixChan  	=FLP_Word+18;
FLP_Resonance	=FLP_Word+19;
FLP_LoopBar  	=FLP_Word+20;
FLP_StDel    	=FLP_Word+21;
FLP_FX3      	=FLP_Word+22;
FLP_DotReso  	=FLP_Word+23;
FLP_DotCutOff	=FLP_Word+24;
FLP_ShiftDelay	=FLP_Word+25;
FLP_LoopEndBar	=FLP_Word+26;
FLP_Dot      	=FLP_Word+27;
FLP_DotShift 	=FLP_Word+28;

// DWORD EVENTS
FLP_Int      	=128;
FLP_Color    	=FLP_Int;
FLP_PlayListItem	=FLP_Int+1;      	//+Pos (word) +PatNum (word)
FLP_Echo     	=FLP_Int+2;
FLP_FXSine   	=FLP_Int+3;
FLP_CutCutBy 	=FLP_Int+4;
FLP_WindowH  	=FLP_Int+5;
FLP_MiddleNote	=FLP_Int+7;
FLP_Reserved  	=FLP_Int+8;        	// may contain an invalid version info
FLP_MainResoCutOff	=FLP_Int+9;
FLP_DelayReso	=FLP_Int+10;
FLP_Reverb   	=FLP_Int+11;
FLP_IntStretch	=FLP_Int+12;
FLP_SSNote   	=FLP_Int+13;                      
FLP_FineTune 	=FLP_Int+14;

// TEXT EVENTS
FLP_Undef    	=192;            	//+Size (var length)
FLP_Text     	=FLP_Undef;         	//+Size (var length)+Text (Null Term. String)
FLP_Text_ChanName  	=FLP_Text;	// name for the current channel
FLP_Text_PatName   	=FLP_Text+1;	// name for the current pattern
FLP_Text_Title     	=FLP_Text+2;	// title of the loop
FLP_Text_Comment   	=FLP_Text+3;	// old comments in text format. Not used anymore
FLP_Text_SampleFileName	=FLP_Text+4;	// filename for the sample in the current channel, stored as relative path
FLP_Text_URL       	=FLP_Text+5;
FLP_Text_CommentRTF	=FLP_Text+6;  	// new comments in Rich Text format
FLP_Version        	=FLP_Text+7;
FLP_Text_PluginName	=FLP_Text+9;	// plugin file name (without path)

FLP_MIDICtrls      	=FLP_Text+16;
FLP_Delay          	=FLP_Text+17;
FLP_TS404Params    	=FLP_Text+18;
FLP_DelayLine      	=FLP_Text+19;
FLP_NewPlugin      	=FLP_Text+20;
FLP_PluginParams   	=FLP_Text+21;
FLP_ChanParams    	=FLP_Text+23; 	// block of various channel params (can grow)
More details to come, if anyone is interested... If not, let's not spend too
much time into this file :)

----------------------------------------------------------------
Fruity Wrapper
There are two formats, the old and the new :)

Both start with a version number (signed long).

The old format (version <= 4):
- optional "extra block" size (dword)
- midi port (dword)
- synth saved or not (dword)
- plugin type (dword) - vst (0 or 1), dx (1 or 5), vst 3 (7 or 8)
- plugin specific block size (dword)
- plugin specific block:
  (vst): name (1 byte length + data)
  (dx): name (1 byte length + data) + guid (16 bytes)
- ... (since you want the name, I'll leave this out)

The new format (version > 4) is based on chunks. A chunk is defined by an ID (dword) and a size (int64). After that comes chunk specific data. You can skip chunks based on the size.

The plugin name is stored in a chunk with ID = 54 (cidPluginName). I think you can just skip all other chunks.
```
