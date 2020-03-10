# Benemérita Universidad Autónoma de Puebla
# Facultad de Ciencias de la Computación
#
# Herramienta de formato para dar formato a tablas
# exportadas de Oracle SQL Developer (o cualquier otro DBMS)
#
# El archivo puede ser en cualquier formato, pero debe ser un archivo de valores
# separados por comas (CSV), modificado de tal forma que en la cabecera aparezca
# el nombre de los atributos, junto con el rango del tamaño del dato (que se pueden obtener
# a través de Oracle SQL Developer o el DBMS correspondiente)
#
# El resultado será un archivo de texto legible por el programa de consultas
#
# Uso:
# py format.py <ruta y nombre de la tabla exportada desde el DBMS> <ruta y nombre del archivo de salida>
# Nota: El nombre del archivo de salida será también el nombre de la tabla en
# el programa de consultas
#
# ~Coria

import sys

def format():
    if (len(sys.argv) < 3):
        print("Uso:\npy format.py <ruta y nombre de la tabla exportada> <ruta y nombre del archivo de salida>\n");
        exit();

    f = open(sys.argv[1], 'r') # Archivo de entrada
    o = open(sys.argv[2], 'w') # Archivo de salida

    header = f.readline()

    # Genera la cabecera
    header = header.replace('"', '').replace('\n', '').lower()
    o.write(header + '\n')

    # Obtiene los atributos y sus rangos
    attribs = header.split(',')
    lens = []
    for i in range(2, len(attribs), 3):
        lens.append(int(attribs[i]) - int(attribs[i - 1]) + 1)

    # Escribe el nuevo archivo en la ruta indicada
    for l in f.readlines():
        values = l.replace('"', '').replace('\n', '').split(',')
        newline = ''
        for i in range(0, len(values)):
            for j in range(0, lens[i] - len(values[i])):
                newline = newline + ' '
            newline = newline + values[i]
            
        o.write(newline + '\n')
    
    f.close()
    o.close()

if __name__ == "__main__":
    format()