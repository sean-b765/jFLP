package org.sean.event;

public class Events {
    // BYTE EVENTS
    public static final int FLP_Byte = 0;
    public static final int FLP_Enabled = 0;
    public static final int FLP_NoteOn = 1; //+pos (byte)
    public static final int FLP_Vol = 2;
    public static final int FLP_Pan = 3;
    public static final int FLP_MIDIChan = 4;
    public static final int FLP_MIDINote = 5;
    public static final int FLP_MIDIPatch = 6;
    public static final int FLP_MIDIBank = 7;
    public static final int FLP_LoopActive = 9;
    public static final int FLP_ShowInfo = 10;
    public static final int FLP_Shuffle = 11;
    public static final int FLP_MainVol = 12;
    public static final int FLP_Stretch = 13; // old byte version
    public static final int FLP_Pitchable = 14;
    public static final int FLP_Zipped = 15;
    public static final int FLP_Delay_Flags = 16;
    public static final int FLP_PatLength = 17;
    public static final int FLP_BlockLength = 18;
    public static final int FLP_UseLoopPoints = 19;
    public static final int FLP_LoopType = 20;
    public static final int FLP_ChanType = 21;
    public static final int FLP_MixSliceNum = 22;
    public static final int FLP_EffectChannelMuted = 27;

    // WORD EVENTS
    public static final int FLP_Word = 64;
    public static final int FLP_NewChan = FLP_Word;
    public static final int FLP_NewPat = FLP_Word + 1; //+PatNum (word)
    public static final int FLP_Tempo = FLP_Word + 2;
    public static final int FLP_CurrentPatNum = FLP_Word + 3;
    public static final int FLP_PatData = FLP_Word + 4;
    public static final int FLP_FX = FLP_Word + 5;
    public static final int FLP_Fade_Stereo = FLP_Word + 6;
    public static final int FLP_CutOff = FLP_Word + 7;
    public static final int FLP_DotVol = FLP_Word + 8;
    public static final int FLP_DotPan = FLP_Word + 9;
    public static final int FLP_PreAmp = FLP_Word + 10;
    public static final int FLP_Decay = FLP_Word + 11;
    public static final int FLP_Attack = FLP_Word + 12;
    public static final int FLP_DotNote = FLP_Word + 13;
    public static final int FLP_DotPitch = FLP_Word + 14;
    public static final int FLP_DotMix = FLP_Word + 15;
    public static final int FLP_MainPitch = FLP_Word + 16;
    public static final int FLP_RandChan = FLP_Word + 17;
    public static final int FLP_MixChan = FLP_Word + 18;
    public static final int FLP_Resonance = FLP_Word + 19;
    public static final int FLP_LoopBar = FLP_Word + 20;
    public static final int FLP_StDel = FLP_Word + 21;
    public static final int FLP_FX3 = FLP_Word + 22;
    public static final int FLP_DotReso = FLP_Word + 23;
    public static final int FLP_DotCutOff = FLP_Word + 24;
    public static final int FLP_ShiftDelay = FLP_Word + 25;
    public static final int FLP_LoopEndBar = FLP_Word + 26;
    public static final int FLP_Dot = FLP_Word + 27;
    public static final int FLP_DotShift = FLP_Word + 28;
    public static final int FLP_LayerChans = FLP_Word + 30;

    // DWORD EVENTS
    public static final int FLP_Int = 128;
    public static final int FLP_Color = FLP_Int;
    public static final int FLP_PlayListItem = FLP_Int + 1; //+Pos (word) +PatNum (word)
    public static final int FLP_Echo = FLP_Int + 2;
    public static final int FLP_FXSine = FLP_Int + 3;
    public static final int FLP_CutCutBy = FLP_Int + 4;
    public static final int FLP_WindowH = FLP_Int + 5;
    public static final int FLP_MiddleNote = FLP_Int + 7;
    public static final int FLP_Reserved = FLP_Int + 8; // may contain an invalid version info
    public static final int FLP_MainResoCutOff = FLP_Int + 9;
    public static final int FLP_DelayReso = FLP_Int + 10;
    public static final int FLP_Reverb = FLP_Int + 11;
    public static final int FLP_IntStretch = FLP_Int + 12;
    public static final int FLP_SSNote = FLP_Int + 13;
    public static final int FLP_FineTune = FLP_Int + 14;
    public static final int FLP_FineTempo = 156;

    // TEXT EVENTS
    public static final int FLP_Undef = 192; //+Size (char length)
    public static final int FLP_Text = FLP_Undef; //+Size (char length)+Text  (Null Term. String)
    public static final int FLP_Text_ChanName = FLP_Text; // name for the current channel
    public static final int FLP_Text_PatName = FLP_Text + 1; // name for the current pattern
    public static final int FLP_Text_Title = FLP_Text + 2; // title of the loop
    public static final int FLP_Text_Comment = FLP_Text + 3; // old comments in text format.
    // Not used anymore
    public static final int FLP_Text_SampleFileName = FLP_Text + 4; // filename for the sample in the current channel, stored as relative path
    public static final int FLP_Text_URL = FLP_Text + 5;
    public static final int FLP_Text_CommentRTF = FLP_Text + 6; // new comments in Rich Text format
    public static final int FLP_Text_Version = FLP_Text + 7;
    public static final int FLP_Text_PluginName = FLP_Text + 9; // plugin file name (without path)

    public static final int FLP_Text_EffectChanName = FLP_Text + 12;
    public static final int FLP_Text_MIDICtrls = FLP_Text + 16;
    public static final int FLP_Text_Delay = FLP_Text + 17;
    public static final int FLP_Text_TS404Params = FLP_Text + 18;
    public static final int FLP_Text_DelayLine = FLP_Text + 19;
    public static final int FLP_Text_NewPlugin = FLP_Text + 20;
    public static final int FLP_Text_PluginParams = FLP_Text + 21;
    public static final int FLP_Text_ChanParams = FLP_Text + 23;// block of various channel params (can grow)
    public static final int FLP_Text_EnvLfoParams = FLP_Text + 26;
    public static final int FLP_Text_BasicChanParams= FLP_Text + 27;
    public static final int FLP_Text_OldFilterParams= FLP_Text + 28;
    public static final int FLP_Text_AutomationData = FLP_Text + 31;
    public static final int FLP_Text_PatternNotes = FLP_Text + 32;
    public static final int FLP_Text_ChanGroupName = FLP_Text + 39;
    public static final int FLP_Text_PlayListItems = FLP_Text + 41;
}
