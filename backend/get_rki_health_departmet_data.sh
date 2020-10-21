#!/bin/sh

# Downloads the RKI data about the german health departments from the RKI website and shortens them by the currently not needed place names.
# The origin XML has UTF16 as encoding and sed uses UTF8 on my computer. The encoding must first be adjusted for sed to work properly.

echo "
Attention: There are always discrepancies in the provision of data at the RKI. Please check the following links to make sure you are downloading the latest data!
https://www.rki.de/DE/Content/Infekt/IfSG/Software/software_node.html (Last part "Aktueller Datenbestand des PLZ-Tools")
https://survnet.rki.de/Content/Service/DownloadHandler.ashx?Id=82 (Name of the ZIP file)
https://www.rki.de/DE/Content/Infekt/IfSG/Software/Aktueller_Datenbestand.html
"

wget -q -O tmp.zip https://survnet.rki.de/Content/Service/DownloadHandler.ashx?Id=82
unzip tmp.zip 
rm tmp.zip

FILENAME="TransmittingSiteSearchText.xml"

iconv -f UTF-16 -t UTF-8 $FILENAME | sed -e 's/<SearchText Value="[^0-9>]*"\/>//g' > src/main/resources/masterdata/$FILENAME
rm $FILENAME

echo "The created shorted XML file is src/main/resources/masterdata/$FILENAME!" 
