# trace-alignment-for-dts

## Overview

This repository includes a set of algorithms and auxiliary methods to validate the behavior of a digital twin (DT)
against its physical counterpart. This validation is based on a trace alignment algorithm named Needleman-Wunsch 
that is usually employed for the alignment of character traces common in fields like bioinformatics to represent 
proteins. Our approach adapts this algorithm to align the traces that describe the behavior of out DT system.

## Repository structure

### Alignment

The **methods** regarding the trace alignment are located in: [/trace-alignment-fot-dts/src/main/java/alignment/](https://github.com/atenearesearchgroup/trace-alignment-for-dts/tree/main/trace-alignment-fot-dts/src/main/java/alignment)

The **synthetic traces** used for the alignments are located in: [/trace-alignment-fot-dts/src/main/resources/input/](https://github.com/atenearesearchgroup/trace-alignment-for-dts/tree/main/trace-alignment-fot-dts/src/main/resources/input)

The **alignments** performed from the synthetic traces are located in: [/trace-alignment-fot-dts/src/main/resources/output/](https://github.com/atenearesearchgroup/trace-alignment-for-dts/tree/main/trace-alignment-fot-dts/src/main/resources/output)

### Similarity measures

The auxiliary methods that measure the distance between snapshots and produce the alignment plots are available in: [/trace-alignment-fot-dts/src/main/python/](https://github.com/atenearesearchgroup/trace-alignment-for-dts/tree/main/trace-alignment-fot-dts/src/main/python)

## Acknowledgments

The original version of the Needleman-Wunsch algorithm was taken from the [NeoBio](http://neobio.sourceforge.net/) library.

