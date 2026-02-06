<div align="center">

# 🐍 Snake Race — Segundo Laboratorio de Hilos

### 🏫 Escuela Colombiana de Ingeniería Julio Garavito  
### 📚 Arquitecturas de Software (ARSW)  

**Laboratorio No. 2 — Prime Finder & Snake Race**

---

<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
<img src="https://img.shields.io/badge/Threads-Concurrency-blue?style=for-the-badge" alt="Threads">
<img src="https://img.shields.io/badge/Status-Completed-success?style=for-the-badge" alt="Status">

</div>

---

## 📂 Estructura del Repositorio
```
📦 Snake Race Lab
├── 📁 snake-game/                    # Módulo del juego Snake Race
│   ├── 📁 src/main/java/
│   │   └── 📁 co/eci/snake/
│   │       ├── 📁 app/              # Punto de entrada (Main)
│   │       ├── 📁 concurrency/      # Lógica de hilos y concurrencia
│   │       ├── 📁 core/             # Motor del juego y entidades
│   │       └── 📁 ui/               # Interfaz gráfica
│   └── 📄 pom.xml                   # Configuración Maven del juego
│
├── 📁 primeFinder/                   # Módulo del buscador de números primos
│   ├── 📁 src/main/java/
│   │   └── 📁 edu/eci/arsw/primefinder/
│   │       ├── 📄 Main.java         # Punto de entrada
│   │       ├── 📄 Control.java      # Controlador de hilos
│   │       └── 📄 PrimeFinderThread.java  # Thread para búsqueda
│   └── 📄 pom.xml                   # Configuración Maven de Prime Finder
│
├── 📁 docs/                          # Documentación del laboratorio
├── 📄 pom.xml                        # POM padre (multi-módulo)
└── 📄 README.md                      # Este archivo
```

---

## 💻 Compilar Todo el Proyecto

```bash
mvn clean compile
```

---

## 🎮 Ejecutar Snake Race

### Ejecución básica (2 serpientes por defecto)

Desde la carpeta `snake-game`:
```bash
cd snake-game
mvn exec:java
```

Desde la raíz del proyecto:
```bash
mvn exec:java -pl snake-game
```

### Ejecución con número personalizado de serpientes

Para especificar el número de serpientes (por ejemplo, 4 serpientes):

Desde la carpeta `snake-game`:
```bash
cd snake-game
mvn -q -DskipTests exec:java -Dsnakes=4
```

Desde la raíz del proyecto:
```bash
mvn -q -DskipTests exec:java -pl snake-game -Dsnakes=4
```

---

## 🔢 Ejecutar Prime Finder

Desde la carpeta `primeFinder`:
```bash
cd primeFinder
mvn exec:java
```

Desde la raíz del proyecto:
```bash
mvn exec:java -pl primeFinder
```

---

## 👥 Autores

<table align="center">
  <tr>
    <td align="center">
      <strong>María Belén Quintero</strong><br>
      👩‍💻
    </td>
    <td align="center">
      <strong>Nikolas Martínez Rivera</strong><br>
      👨‍💻
    </td>
  </tr>
</table>

---

## 📌 Notas Importantes

> 📄 **Documentación completa:** Las respuestas al laboratorio se encuentran en la carpeta **`docs/`**

> ⚠️ **Ejercicio de calentamiento:** En caso de problemas al ejecutar el PrimeFinder, consulta la solución propuesta:  
> 🔗 [**Repositorio PrimeFinder**](https://github.com/mbquial/Martinez-Quintero-wait-notify-excercise.git)